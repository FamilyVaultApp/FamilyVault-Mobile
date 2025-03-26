package com.github.familyvault.backend.client

import com.github.familyvault.models.FamilyMember
import com.github.familyvault.models.PublicPrivateKeyPair
import com.github.familyvault.models.enums.FamilyGroupMemberPermissionGroup
import com.simplito.java.privmx_endpoint.model.UserWithPubKey
import com.simplito.java.privmx_endpoint.modules.thread.ThreadApi
import com.simplito.java.privmx_endpoint_extra.lib.PrivmxEndpoint
import com.simplito.java.privmx_endpoint_extra.lib.PrivmxEndpointContainer
import com.simplito.java.privmx_endpoint_extra.model.Modules

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
                members.first,
                members.second,
                publicMeta,
                privateMeta
            )

            println("ThreadId: $threadId")
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
}