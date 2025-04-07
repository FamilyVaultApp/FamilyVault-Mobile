package com.github.familyvault.backend.client

import com.github.familyvault.AppConfig
import com.github.familyvault.backend.exceptions.FamilyVaultPrivMxException
import com.github.familyvault.models.PublicEncryptedPrivateKeyPair
import com.github.familyvault.models.chat.MessageItem
import com.github.familyvault.models.chat.MessagePublicMeta
import com.github.familyvault.models.chat.ThreadId
import com.github.familyvault.models.chat.ThreadItem
import com.github.familyvault.utils.EncryptUtils
import com.simplito.java.privmx_endpoint.model.UserWithPubKey
import com.simplito.java.privmx_endpoint.model.exceptions.PrivmxException
import com.simplito.java.privmx_endpoint.modules.thread.ThreadApi
import com.simplito.java.privmx_endpoint_extra.lib.PrivmxEndpoint
import com.simplito.java.privmx_endpoint_extra.lib.PrivmxEndpointContainer
import com.simplito.java.privmx_endpoint_extra.model.Modules
import com.simplito.java.privmx_endpoint_extra.model.SortOrder
import kotlinx.serialization.json.Json
import kotlin.random.Random


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
        users: List<Pair<String, String>>,
        managers: List<Pair<String, String>>,
        threadTags: ByteArray,
        threadName: ByteArray
    ): ThreadId {
        val userList: List<UserWithPubKey> = users.map { (username, publicKey) ->
            UserWithPubKey(username, publicKey)
        }
        val managerList: List<UserWithPubKey> = managers.map { (username, publicKey) ->
            UserWithPubKey(username, publicKey)
        }

        val threadId = threadApi?.createThread(
            contextId,
            managerList,
            userList,
            threadTags,
            threadName
        )

        if (threadId == null) {
            throw Exception("Received empty threadsPagingList")
        }
        return ThreadId(threadId)
    }

    override fun retrieveAllThreads(
        contextId: String,
        startIndex: Long,
        pageSize: Long
    ): List<ThreadItem> {
        val threadsList: MutableList<ThreadItem> = mutableListOf()
        if (threadApi == null) {
            throw Exception("ThreadApi is null")
        }
        val threadsPagingList = threadApi?.listThreads(
            contextId,
            startIndex,
            pageSize,
            SortOrder.DESC
        )

        if (threadsPagingList == null) {
            throw Exception("Received empty threadsPagingList")
        }
        threadsPagingList.readItems.map {
            threadsList.add(
                ThreadItem(
                    it.threadId,
                    it.managers,
                    it.users,
                    it.privateMeta.decodeToString(),
                    it.publicMeta.decodeToString()
                )
            )
        }

        return threadsList
    }

    override fun sendMessage(messageContent: String, threadId: ThreadId, responseToMsgId: String) {
        val publicMeta: Any = if (responseToMsgId.isNotEmpty()) {
            MessagePublicMeta(responseTo = responseToMsgId)
        } else {
            ByteArray(0)
        }
        val privateMeta = ByteArray(0)
        if (threadApi == null) {
            throw Exception("Thread API is null")
        }
        threadApi!!.sendMessage(
            threadId.threadId,
            Json.encodeToString(publicMeta).encodeToByteArray(),
            privateMeta,
            messageContent.encodeToByteArray()
        )

    }

    override fun retrieveMessagesFromThread(
        threadId: ThreadId,
        startIndex: Long,
        pageSize: Long
    ): List<MessageItem> {
        if (threadApi == null) {
            throw Exception("Received empty threadApi")
        }

        val messagesPagingList = threadApi?.listMessages(
            threadId.threadId,
            startIndex,
            pageSize,
            SortOrder.DESC
        )

        if (messagesPagingList == null) {
            throw Exception("Messages paging list empty.")
        }

        val messages = messagesPagingList.readItems?.map {
            MessageItem(
                it.data.contentToString(),
                it.authorPubKey,
                it.data.decodeToString(),
                Json.decodeFromString(it.publicMeta.decodeToString())
            )
        }

        if (messages == null) {
            throw Exception("Messages list empty.")
        }

        return messages
    }

}

