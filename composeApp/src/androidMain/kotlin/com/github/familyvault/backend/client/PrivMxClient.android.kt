package com.github.familyvault.backend.client

import com.github.familyvault.AppConfig
import com.github.familyvault.backend.exceptions.FamilyVaultPrivMxException
import com.github.familyvault.backend.models.PrivMxUser
import com.github.familyvault.backend.models.StoreItem
import com.github.familyvault.backend.models.StorePublicMeta
import com.github.familyvault.backend.models.ThreadId
import com.github.familyvault.backend.models.ThreadItem
import com.github.familyvault.backend.models.ThreadMessageItem
import com.github.familyvault.backend.models.ThreadMessagePrivateMeta
import com.github.familyvault.backend.models.ThreadPrivateMeta
import com.github.familyvault.backend.models.ThreadPublicMeta
import com.github.familyvault.backend.utils.StoreMetaEncoder
import com.github.familyvault.backend.utils.ThreadMessageEncoder
import com.github.familyvault.backend.utils.ThreadMetaDecoder
import com.github.familyvault.backend.utils.ThreadMetaEncoder
import com.github.familyvault.models.PublicEncryptedPrivateKeyPair
import com.github.familyvault.models.enums.chat.ThreadIconType
import com.github.familyvault.utils.EncryptUtils
import com.github.familyvault.utils.mappers.PrivMxMessageToMessageItemMapper
import com.github.familyvault.utils.mappers.PrivMxStoreToStoreItemMapper
import com.github.familyvault.utils.mappers.PrivMxThreadToThreadItemMapper
import com.simplito.java.privmx_endpoint.model.UserWithPubKey
import com.simplito.java.privmx_endpoint.model.exceptions.PrivmxException
import com.simplito.java.privmx_endpoint.modules.store.StoreApi
import com.simplito.java.privmx_endpoint.modules.thread.ThreadApi
import com.simplito.java.privmx_endpoint_extra.events.EventType
import com.simplito.java.privmx_endpoint_extra.lib.PrivmxEndpoint
import com.simplito.java.privmx_endpoint_extra.lib.PrivmxEndpointContainer
import com.simplito.java.privmx_endpoint_extra.model.Modules
import com.simplito.java.privmx_endpoint_extra.model.SortOrder
import com.simplito.java.privmx_endpoint_extra.storeFileStream.StoreFileStream
import com.simplito.java.privmx_endpoint_extra.storeFileStream.StoreFileStreamReader
import com.simplito.java.privmx_endpoint_extra.storeFileStream.StoreFileStreamWriter
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
    private var storeApi: StoreApi? = null

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
        storeApi = connection!!.storeApi
    }

    override fun disconnect() {
        connection?.unregisterAll()
        connection?.close()
    }

    override fun createThread(
        contextId: String,
        users: List<PrivMxUser>,
        managers: List<PrivMxUser>,
        tag: String,
        type: String,
        name: String,
        referenceStoreId: String?,
        threadIcon: ThreadIconType?,
        threadInitialCreators: List<PrivMxUser>
    ): String {
        val userList: List<UserWithPubKey> = users.map { (userId, publicKey) ->
            UserWithPubKey(userId, publicKey)
        }
        val managerList: List<UserWithPubKey> = managers.map { (userId, publicKey) ->
            UserWithPubKey(userId, publicKey)
        }
        val threadId = threadApi?.createThread(
            contextId,
            userList,
            managerList,
            ThreadMetaEncoder.encode(ThreadPublicMeta(tag, type)),
            ThreadMetaEncoder.encode(
                ThreadPrivateMeta(
                    name, referenceStoreId, threadIcon, threadInitialCreators.map { it.publicKey })
            )
        )

        return requireNotNull(threadId) { "Received empty threadsPagingList" }
    }

    override fun createStore(
        contextId: String, users: List<PrivMxUser>, managers: List<PrivMxUser>, type: String
    ): String {
        val userList: List<UserWithPubKey> = users.map { (userId, publicKey) ->
            UserWithPubKey(userId, publicKey)
        }
        val managerList: List<UserWithPubKey> = managers.map { (userId, publicKey) ->
            UserWithPubKey(userId, publicKey)
        }

        val storeId = storeApi?.createStore(
            contextId,
            userList,
            managerList,
            StoreMetaEncoder.encode(StorePublicMeta(type)),
            ByteArray(0)
        )

        return requireNotNull(storeId) { "Received empty storeId" }
    }

    override fun retrieveThread(
        threadId: String,
    ): ThreadItem {
        val thread = requireNotNull(threadApi).getThread(threadId)

        return PrivMxThreadToThreadItemMapper.map(thread)
    }

    override fun updateThread(
        threadId: String,
        users: List<PrivMxUser>,
        managers: List<PrivMxUser>,
        newName: String?,
        threadIcon: ThreadIconType?
    ) {
        val thread = requireNotNull(threadApi?.getThread(threadId)) { "Thread is null" }
        val userList: List<UserWithPubKey> = users.map { (userId, publicKey) ->
            UserWithPubKey(userId, publicKey)
        }
        val managerList: List<UserWithPubKey> = managers.map { (userId, publicKey) ->
            UserWithPubKey(userId, publicKey)
        }

        val decodedPrivateMeta = ThreadMetaDecoder.decodePrivateMeta(thread.privateMeta)
        val privateMeta = ThreadMetaEncoder.encode(
            decodedPrivateMeta.copy(
                name = newName ?: decodedPrivateMeta.name,
                threadIcon = threadIcon ?: decodedPrivateMeta.threadIcon,
                initialManagersPublicKeys = decodedPrivateMeta.initialManagersPublicKeys
            )
        )

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

    override fun updateStore(
        storeId: String,
        users: List<PrivMxUser>,
        managers: List<PrivMxUser>,
    ) {
        val store = requireNotNull(storeApi?.getStore(storeId)) { "Store is null" }

        val userList: List<UserWithPubKey> = users.map { (userId, publicKey) ->
            UserWithPubKey(userId, publicKey)
        }
        val managerList: List<UserWithPubKey> = managers.map { (userId, publicKey) ->
            UserWithPubKey(userId, publicKey)
        }

        storeApi?.updateStore(
            storeId,
            userList,
            managerList,
            store.publicMeta,
            store.privateMeta,
            store.version,
            true,
            false
        )
    }

    override fun deleteThread(threadId: String) {
        val threadApi = requireNotNull(threadApi)

        threadApi.deleteThread(threadId)
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

    override fun retrieveAllThreadsWithTag(
        contextId: String, tag: String, startIndex: Int, pageSize: Int
    ): List<ThreadItem> {
        val threads = retrieveAllThreads(contextId, startIndex, pageSize)

        return threads.filter { it.publicMeta.tag == tag }
    }

    override fun retrieveAllStores(contextId: String, skip: Int, limit: Int): List<StoreItem> {
        val stores = requireNotNull(storeApi).listStores(
            contextId, skip.toLong(), limit.toLong(), SortOrder.DESC
        ).readItems

        return stores.map {
            PrivMxStoreToStoreItemMapper.map(it)
        }
    }

    override fun retrieveAllStoresWithType(
        contextId: String, type: String, skip: Int, limit: Int
    ): List<StoreItem> {
        val stores = retrieveAllStores(contextId, skip, limit)
        return stores.filter { it.publicMeta.type == type }
    }

    override fun sendMessage(
        threadId: String, content: String, type: String, referenceMessageId: String
    ) {
        sendMessage(threadId, content.encodeToByteArray(), type, referenceMessageId)
    }

    override fun sendMessage(
        threadId: String, content: ByteArray, type: String, referenceMessageId: String
    ) {
        val threadApi = requireNotNull(threadApi)
        val publicMeta = ByteArray(0)
        val privateMeta = ThreadMessagePrivateMeta(type)

        threadApi.sendMessage(
            threadId, publicMeta, ThreadMessageEncoder.encode(privateMeta), content
        )
    }

    override fun updateMessageContent(
        messageId: String,
        content: String,
    ) {
        val threadApi = requireNotNull(threadApi)

        val message = threadApi.getMessage(messageId)
        threadApi.updateMessage(
            messageId, message.publicMeta, message.privateMeta, content.encodeToByteArray()
        )
    }

    override fun getFileAsByteArrayFromStore(fileId: String): ByteArray {
        var data = ByteArray(0)

        StoreFileStreamReader.openFile(
            storeApi,
            fileId,
        ).also {
            do {
                val chunk = it.read(StoreFileStream.OPTIMAL_SEND_SIZE)
                data += chunk
            } while (chunk.size.toLong() == StoreFileStream.OPTIMAL_SEND_SIZE)
        }.close()

        return data
    }

    override fun getFilesAsByteArrayFromStore(
        storeId: String?, limit: Long, skip: Long
    ): List<ByteArray> {
        val files = storeApi!!.listFiles(
            storeId, skip, limit, SortOrder.DESC
        )

        return files.readItems.mapNotNull { file ->
            try {
                getFileAsByteArrayFromStore(file.info.fileId)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    override fun sendByteArrayToStore(
        storeId: String, content: ByteArray
    ): String {
        val fileId = StoreFileStreamWriter.createFile(
            storeApi,
            storeId,
            ByteArray(0),
            ByteArray(0),
            content.size.toLong(),
        ).also {
            it.write(content)
        }.close()

        return fileId
    }

    override fun retrieveMessagesFromThread(
        threadId: String, startIndex: Int, pageSize: Int
    ): List<ThreadMessageItem> {
        val threadApi = requireNotNull(threadApi)

        val messagesPagingList = threadApi.listMessages(
            threadId, startIndex.toLong(), pageSize.toLong(), SortOrder.DESC
        )

        val messages = messagesPagingList.readItems?.map {
            PrivMxMessageToMessageItemMapper.map(it)
        }

        return messages ?: emptyList()
    }

    override fun retrieveLastMessageFromThread(threadId: String): ThreadMessageItem? {
        val threadApi = requireNotNull(threadApi)

        val messages = threadApi.listMessages(threadId, 0, 1, SortOrder.DESC).readItems

        if (messages.isEmpty()) {
            return null
        }

        return PrivMxMessageToMessageItemMapper.map(messages.first())
    }

    override fun retrieveMessageById(
        messageId: String
    ): ThreadMessageItem {
        val threadApi = requireNotNull(threadApi)

        return PrivMxMessageToMessageItemMapper.map(threadApi.getMessage(messageId))
    }

    /* Listeners */
    override fun unregisterAllEvents(eventName: String) {
        requireNotNull(connection).unregisterCallbacks(eventName)
    }

    override fun registerOnMessageCreated(
        eventName: String, threadId: String, callback: (ThreadMessageItem) -> Unit
    ) {
        requireNotNull(connection).registerCallback(
            eventName, EventType.ThreadNewMessageEvent(threadId)
        ) {
            callback(PrivMxMessageToMessageItemMapper.map(it))
        }
    }

    override fun registerOnMessageUpdate(
        eventName: String, threadId: String, callback: (ThreadMessageItem) -> Unit
    ) {
        requireNotNull(connection).registerCallback(
            eventName, EventType.ThreadMessageUpdatedEvent(threadId)
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

    override fun registerOnThreadDeleted(eventName: String, callback: (ThreadId) -> Unit) {
        requireNotNull(connection).registerCallback(
            eventName, EventType.ThreadDeletedEvent
        ) {
            callback(ThreadId(it.threadId))
        }
    }

    override fun registerOnStoreFileCreated(
        eventName: String, storeId: String, callback: (ByteArray) -> Unit
    ) {
        requireNotNull(connection).registerCallback(
            eventName, EventType.StoreFileCreatedEvent(storeId)
        ) { newFile ->
            callback(getFileAsByteArrayFromStore(newFile.info.fileId))
        }
    }

    override fun close() {
        requireNotNull(connection).unregisterAll()
        requireNotNull(connection).close()
    }
}