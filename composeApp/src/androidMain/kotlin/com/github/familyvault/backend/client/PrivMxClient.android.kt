package com.github.familyvault.backend.client

import com.github.familyvault.models.FamilyMember
import com.github.familyvault.models.PublicPrivateKeyPair
import com.github.familyvault.models.enums.FamilyGroupMemberPermissionGroup
import com.simplito.java.privmx_endpoint.model.UserWithPubKey
import com.simplito.java.privmx_endpoint.modules.thread.ThreadApi
import com.simplito.java.privmx_endpoint_extra.lib.PrivmxEndpoint
import com.simplito.java.privmx_endpoint_extra.lib.PrivmxEndpointContainer
import com.simplito.java.privmx_endpoint_extra.model.Modules
import com.simplito.java.privmx_endpoint_extra.model.SortOrder
import kotlinx.serialization.json.Json


internal class PrivMxClient(certsPath: String) :
    IPrivMxClient {
    private val initModules = setOf(
        Modules.THREAD,
        Modules.STORE,
        Modules.INBOX
    )
    private val container: PrivmxEndpointContainer = PrivmxEndpointContainer().also {
        it.setCertsPath(certsPath)
    }
    private var connection: PrivmxEndpoint? = null
    private var threadApi: ThreadApi? = null

    override fun generatePairOfPrivateAndPublicKey(
        secret: String,
        salt: String
    ): PublicPrivateKeyPair {
        val privateKey = container.cryptoApi.derivePrivateKey(secret, salt)
        val publicKey = container.cryptoApi.derivePublicKey(privateKey)

        return PublicPrivateKeyPair(publicKey, privateKey)
    }

    override fun establishConnection(bridgeUrl: String, solutionId: String, privateKey: String) {
        connection = container.connect(
            initModules,
            privateKey,
            solutionId,
            bridgeUrl
        )
        threadApi = connection!!.threadApi
    }

    override fun createThread(contextId: String, familyGroupMembers: List<FamilyMember>, publicMeta: ByteArray, privateMeta: ByteArray): Boolean {

        val members = splitFamilyMembers(familyGroupMembers)
        try {
            val threadId = threadApi?.createThread(
                contextId,
                members.second,
                members.first,
                publicMeta,
                "Nowy czat".encodeToByteArray() // TODO: Dodać opcję dodania nazwy czatu
            )

            if (threadId != null) {
                return true
            } else {
                return false
            }
        } catch (e: Exception) {
            println(e.message)
        }
        return false
    }

    private fun splitFamilyMembers(familyGroupMembers: List<FamilyMember>): Pair<List<UserWithPubKey>, List<UserWithPubKey>> {
        var guardians: List<UserWithPubKey> = listOf()
        var members: List<UserWithPubKey> = listOf()

        for (member in familyGroupMembers) {
            if (member.permissionGroup == FamilyGroupMemberPermissionGroup.Guardian) {
                guardians += UserWithPubKey(member.fullname, member.publicKey)
            } else {
                members += UserWithPubKey(member.fullname, member.publicKey)
            }
        }

        return guardians to members
    }

    override fun retrieveAllThreadIds(contextId: String): List<String> {
        val startIndex = 0L
        val pageSize = 100L
        var threadIdList: List<String> = listOf()
        if (threadApi != null)
        {
            val threadsPagingList = threadApi!!.listThreads(
                contextId,
                startIndex,
                pageSize,
                SortOrder.DESC
            )
            threadsPagingList.readItems.map {
                ThreadItem(
                    it,
                    it.privateMeta.decodeToString(),
                    it.publicMeta.decodeToString()
                )
                threadIdList += it.threadId
            }
        }
        return threadIdList
    }

    override fun sendMessage(messageContent: String, threadId: String, responseToMsgId: String): Boolean {
        val publicMeta: Any = if (responseToMsgId.isNotEmpty()) {
            MessagePublicMeta(responseTo = responseToMsgId)
        } else {
            ByteArray(0)
        }
        val privateMeta = ByteArray(0)
        if (threadApi != null) {
            val messageId: String
            try {
                messageId = threadApi!!.sendMessage(
                    threadId,
                    Json.encodeToString(publicMeta).encodeToByteArray(),
                    privateMeta,
                    messageContent.encodeToByteArray()
                )
            } catch (e: Exception) {
                println(e.message)
                return false
            }
            println("Message $messageId created")
            return true
        } else {
            return false
        }
    }

    override fun retrieveMessagesFromThread(threadId: String) {
        val startIndex = 0L
        val pageSize = 100L
        if (threadApi != null) {
            val messagesPagingList = threadApi!!.listMessages(
                threadId,
                startIndex,
                pageSize,
                SortOrder.DESC
            )

            val messages = messagesPagingList.readItems.map {
                MessageItem(
                    it,
                    it.data.decodeToString(),
                    Json.decodeFromString(it.publicMeta.decodeToString())
                )
            }
        }
    }
}