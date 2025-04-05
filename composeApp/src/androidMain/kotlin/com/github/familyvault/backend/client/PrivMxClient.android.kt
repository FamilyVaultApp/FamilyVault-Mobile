package com.github.familyvault.backend.client

import com.github.familyvault.backend.models.PrivMxUser
import com.github.familyvault.backend.models.ThreadId
import com.github.familyvault.backend.models.ThreadItem
import com.github.familyvault.models.PublicPrivateKeyPair
import com.github.familyvault.models.chat.MessageItem
import com.github.familyvault.utils.mappers.PrivMxMessageToMessageItemMapper
import com.github.familyvault.utils.mappers.PrivMxThreadToThreadItemMapper
import com.simplito.java.privmx_endpoint.model.UserWithPubKey
import com.simplito.java.privmx_endpoint.modules.thread.ThreadApi
import com.simplito.java.privmx_endpoint_extra.lib.PrivmxEndpoint
import com.simplito.java.privmx_endpoint_extra.lib.PrivmxEndpointContainer
import com.simplito.java.privmx_endpoint_extra.model.Modules
import com.simplito.java.privmx_endpoint_extra.model.SortOrder

internal class PrivMxClient(certsPath: String) : IPrivMxClient {
    private val initModules = setOf(
        Modules.THREAD, Modules.STORE, Modules.INBOX
    )
    private val container: PrivmxEndpointContainer = PrivmxEndpointContainer().also {
        it.setCertsPath(certsPath)
    }
    private var connection: PrivmxEndpoint? = null
    private var threadApi: ThreadApi? = null

    override fun generatePairOfPrivateAndPublicKey(
        secret: String, salt: String
    ): PublicPrivateKeyPair {
        val privateKey = container.cryptoApi.derivePrivateKey(secret, salt)
        val publicKey = container.cryptoApi.derivePublicKey(privateKey)

        return PublicPrivateKeyPair(publicKey, privateKey)
    }

    override fun establishConnection(bridgeUrl: String, solutionId: String, privateKey: String) {
        connection = container.connect(
            initModules, privateKey, solutionId, bridgeUrl
        )
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
            threadId, startIndex.toLong(), pageSize.toLong(), SortOrder.ASC
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
}

