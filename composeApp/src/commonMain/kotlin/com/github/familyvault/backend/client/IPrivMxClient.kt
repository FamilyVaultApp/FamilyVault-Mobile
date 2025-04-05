package com.github.familyvault.backend.client

import com.github.familyvault.backend.models.PrivMxUser
import com.github.familyvault.models.PublicPrivateKeyPair
import com.github.familyvault.backend.models.MessageItem
import com.github.familyvault.backend.models.ThreadId
import com.github.familyvault.backend.models.ThreadItem

interface IPrivMxClient {
    fun generatePairOfPrivateAndPublicKey(secret: String, salt: String): PublicPrivateKeyPair
    fun establishConnection(bridgeUrl: String, solutionId: String, privateKey: String)
    fun createThread(
        contextId: String,
        users: List<PrivMxUser>,
        managers: List<PrivMxUser>,
        threadTags: String,
        threadName: String
    ): ThreadId

    fun retrieveAllThreads(contextId: String, startIndex: Int, pageSize: Int): List<ThreadItem>
    fun sendMessage(content: String, threadId: String, referenceMessageId: String = "")
    fun retrieveMessagesFromThread(
        threadId: String, startIndex: Int, pageSize: Int
    ): List<MessageItem>

    fun retrieveLastMessageFromThread(threadId: String): MessageItem?
}