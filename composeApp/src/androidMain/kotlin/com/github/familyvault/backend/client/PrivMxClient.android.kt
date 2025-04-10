package com.github.familyvault.backend.client

import com.github.familyvault.AppConfig
import com.github.familyvault.backend.exceptions.FamilyVaultPrivMxException
import com.github.familyvault.backend.models.MessageItem
import com.github.familyvault.backend.models.PrivMxUser
import com.github.familyvault.backend.models.ThreadId
import com.github.familyvault.backend.models.ThreadItem
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
        threadTags: String,
        threadName: String
    ): ThreadId {
        val userList: List<UserWithPubKey> = users.map { (userId, publicKey) ->
            UserWithPubKey(userId, publicKey)
        }
        val managerList: List<UserWithPubKey> = managers.map { (userId, publicKey) ->
            UserWithPubKey(userId, publicKey)
        }

        val threadId = threadApi?.createThread(
            contextId, managerList, userList, // TODO: Poprawić ustawienie manager i user
            threadTags.encodeToByteArray(), threadName.encodeToByteArray()
        )

        if (threadId == null) {
            throw Exception("Received empty threadsPagingList")
        }
        return ThreadId(threadId)
    }

    override fun retrieveAllThreads(
        contextId: String, startIndex: Int, pageSize: Int
    ): List<ThreadItem> {
        val threadsList: MutableList<ThreadItem> = mutableListOf()
        if (threadApi == null) {
            throw Exception("ThreadApi is null")
        }
        val threadsPagingList = threadApi?.listThreads(
            contextId, startIndex.toLong(), pageSize.toLong(), SortOrder.DESC
        )

        if (threadsPagingList == null) {
            throw Exception("Received empty threadsPagingList")
        }
        threadsPagingList.readItems.map {
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
    override fun registerOnMessageCreated(
        threadId: String,
        callback: (MessageItem) -> Unit
    ) {
        requireNotNull(connection).registerCallback(
            "CHAT_EVENT",
            EventType.ThreadNewMessageEvent(threadId)
        ) {
            callback(PrivMxMessageToMessageItemMapper.map(it))
        }
    }

    override fun close() {
        requireNotNull(connection).unregisterAll()
        requireNotNull(connection).close()
    }
}