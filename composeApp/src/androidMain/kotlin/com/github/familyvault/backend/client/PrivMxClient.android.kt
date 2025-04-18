package com.github.familyvault.backend.client

import com.github.familyvault.AppConfig
import com.github.familyvault.backend.exceptions.FamilyVaultPrivMxException
import com.github.familyvault.backend.models.MessageItem
import com.github.familyvault.backend.models.PrivMxUser
import com.github.familyvault.backend.models.ThreadItem
import com.github.familyvault.backend.models.ThreadPrivateMeta
import com.github.familyvault.backend.models.ThreadPublicMeta
import com.github.familyvault.backend.utils.ThreadMetaEncoder
import com.github.familyvault.models.PublicEncryptedPrivateKeyPair
import com.github.familyvault.utils.EncryptUtils
import com.github.familyvault.utils.mappers.PrivMxMessageToMessageItemMapper
import com.github.familyvault.utils.mappers.PrivMxThreadToThreadItemMapper
import com.simplito.java.privmx_endpoint.model.UserWithPubKey
import com.simplito.java.privmx_endpoint.model.exceptions.PrivmxException
import com.simplito.java.privmx_endpoint.modules.thread.ThreadApi
import com.simplito.java.privmx_endpoint_extra.events.EventType
import com.simplito.java.privmx_endpoint_extra.lib.PrivmxEndpoint
import com.simplito.java.privmx_endpoint_extra.lib.PrivmxEndpointContainer
import com.simplito.java.privmx_endpoint_extra.model.Modules
import com.simplito.java.privmx_endpoint_extra.model.SortOrder
import kotlin.random.Random

class PrivMxClient : IPrivMxClient, AutoCloseable {
    private val initModules = setOf(
        Modules.THREAD, Modules.STORE, Modules.INBOX
    )
    private val container: PrivmxEndpointContainer = PrivmxEndpointContainer().also {
        it.startListening()
    }
    private var connection: PrivmxEndpoint? = null
    private var threadApi: ThreadApi? = null

    override fun generatePairOfPrivateAndPublicKey(
        password: String,
    ): PublicEncryptedPrivateKeyPair {
        val privateKey = container.cryptoApi.generatePrivateKey(Random.nextBits(32).toString())
        val publicKey = container.cryptoApi.derivePublicKey(privateKey)
        val encryptedPrivateKey = EncryptUtils.encryptData(
            privateKey, AppConfig.SECRET
        )
        return PublicEncryptedPrivateKeyPair(publicKey, encryptedPrivateKey)
    }

    override fun encryptPrivateKeyPassword(password: String): String {
        return EncryptUtils.encryptData(
            password, AppConfig.SECRET
        )
    }

    override fun decryptPrivateKeyPassword(encryptedPassword: String): String {
        return EncryptUtils.decryptData(
            encryptedPassword, AppConfig.SECRET
        )
    }

    override fun establishConnection(bridgeUrl: String, solutionId: String, privateKey: String) {
        try {
            connection = container.connect(
                initModules, privateKey, solutionId, bridgeUrl
            )
        } catch (e: PrivmxException) {
            throw FamilyVaultPrivMxException(e.code, e.message ?: "")
        }
        threadApi = connection!!.threadApi
    }

    override fun createThread(
        contextId: String,
        users: List<PrivMxUser>,
        managers: List<PrivMxUser>,
        tag: String,
        type: String,
        name: String
    ): String {
        val userList: List<UserWithPubKey> = users.map { (userId, publicKey) ->
            UserWithPubKey(userId, publicKey)
        }
        val managerList: List<UserWithPubKey> = managers.map { (userId, publicKey) ->
            UserWithPubKey(userId, publicKey)
        }

        val threadId = threadApi?.createThread(
            contextId, userList, managerList, // TODO: Poprawić ustawienie manager i user
            ThreadMetaEncoder.encode(ThreadPublicMeta(tag, type)),
            ThreadMetaEncoder.encode(ThreadPrivateMeta(name))
        )

        return requireNotNull(threadId) { "Received empty threadsPagingList" }
    }

    override fun updateThread(
        threadId: String,
        users: List<PrivMxUser>,
        managers: List<PrivMxUser>,
        newName: String?
    ) {
        val thread = requireNotNull(threadApi?.getThread(threadId)) { "Thread is null" }
        val userList: List<UserWithPubKey> = users.map { (userId, publicKey) ->
            UserWithPubKey(userId, publicKey)
        }
        val managerList: List<UserWithPubKey> = managers.map { (userId, publicKey) ->
            UserWithPubKey(userId, publicKey)
        }

        val privateMeta = if (newName != null) {
            ThreadMetaEncoder.encode(ThreadPrivateMeta(newName))
        } else {
            thread.privateMeta
        }

        threadApi?.updateThread(
            thread.threadId,
            userList,
            managerList,
            thread.publicMeta,
            privateMeta,
            thread.version,
            false
        )
    }

    override fun retrieveAllThreads(
        contextId: String, startIndex: Int, pageSize: Int
    ): List<ThreadItem> {
        val threadsList = mutableListOf<ThreadItem>()

        val threadsPagingList = requireNotNull(threadApi).listThreads(
            contextId, startIndex.toLong(), pageSize.toLong(), SortOrder.DESC
        )

        requireNotNull(threadsPagingList).readItems.map {
            threadsList.add(
                PrivMxThreadToThreadItemMapper.map(it)
            )
        }

        return threadsList
    }

    override fun sendMessage(content: String, threadId: String, referenceMessageId: String) {
        val threadApi = requireNotNull(threadApi)

        val publicMeta = ByteArray(0)
        val privateMeta = ByteArray(0)

        threadApi.sendMessage(
            threadId, publicMeta, privateMeta, content.encodeToByteArray()
        )
    }

    override fun retrieveMessagesFromThread(
        threadId: String, startIndex: Int, pageSize: Int
    ): List<MessageItem> {
        val threadApi = requireNotNull(threadApi)

        val messagesPagingList = threadApi.listMessages(
            threadId, startIndex.toLong(), pageSize.toLong(), SortOrder.DESC
        )

        val messages = messagesPagingList.readItems?.map {
            PrivMxMessageToMessageItemMapper.map(it)
        }

        return messages ?: emptyList()
    }

    override fun retrieveLastMessageFromThread(threadId: String): MessageItem? {
        val threadApi = requireNotNull(threadApi)

        val messages = threadApi.listMessages(threadId, 0, 1, SortOrder.DESC).readItems

        if (messages.isEmpty()) {
            return null
        }
        val message = messages.first()

        return PrivMxMessageToMessageItemMapper.map(message)
    }

    /* Listeners */
    override fun unregisterAllEvents(eventName: String) {
        requireNotNull(connection).unregisterCallbacks(eventName)
    }

    override fun registerOnMessageCreated(
        eventName: String,
        threadId: String,
        callback: (MessageItem) -> Unit
    ) {
        requireNotNull(connection).registerCallback(
            eventName, EventType.ThreadNewMessageEvent(threadId)
        ) {
            callback(PrivMxMessageToMessageItemMapper.map(it))
        }
    }

    override fun registerOnThreadCreated(eventName: String, callback: (ThreadItem) -> Unit) {
        requireNotNull(connection).registerCallback(
            eventName, EventType.ThreadCreatedEvent
        ) {
            callback(PrivMxThreadToThreadItemMapper.map(it))
        }
    }

    override fun registerOnThreadUpdated(eventName: String, callback: (ThreadItem) -> Unit) {
        requireNotNull(connection).registerCallback(
            eventName, EventType.ThreadUpdatedEvent
        ) {
            callback(PrivMxThreadToThreadItemMapper.map(it))
        }
    }

    override fun close() {
        requireNotNull(connection).unregisterAll()
        requireNotNull(connection).close()
    }
}