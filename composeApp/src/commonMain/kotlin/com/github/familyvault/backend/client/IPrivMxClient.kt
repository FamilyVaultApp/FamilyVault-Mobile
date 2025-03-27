package com.github.familyvault.backend.client

import com.github.familyvault.models.FamilyMember
import com.github.familyvault.models.PublicPrivateKeyPair

interface IPrivMxClient {
    fun generatePairOfPrivateAndPublicKey(secret: String, salt: String): PublicPrivateKeyPair
    fun establishConnection(bridgeUrl: String, solutionId: String, privateKey: String)
    fun createThread(contextId: String, familyGroupMembers: List<FamilyMember>, publicMeta: ByteArray, privateMeta: ByteArray): Boolean
    fun retrieveAllThreadIds(contextId: String): List<String>
    fun sendMessage(messageContent: String, threadId: String, responseToMsgId: String = ""): Boolean
    fun retrieveMessagesFromThread(threadId: String)
}