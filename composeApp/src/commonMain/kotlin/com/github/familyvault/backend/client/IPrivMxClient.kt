package com.github.familyvault.backend.client

import com.github.familyvault.models.PublicPrivateKeyPair
import com.github.familyvault.models.chat.MessageItem
import com.github.familyvault.models.chat.ThreadId
import com.github.familyvault.models.chat.ThreadItem

interface IPrivMxClient {
    fun generatePairOfPrivateAndPublicKey(secret: String, salt: String): PublicPrivateKeyPair
    fun establishConnection(bridgeUrl: String, solutionId: String, privateKey: String)
    fun createThread(contextId: String, users: List<Pair<String,String>>, managers: List<Pair<String,String>>, threadTags: ByteArray, threadName: ByteArray): ThreadId
    fun retrieveAllThreads(contextId: String, startIndex: Long, pageSize: Long): List<ThreadItem>
    fun sendMessage(messageContent: String, threadId: ThreadId, responseToMsgId: String = "")
    fun retrieveMessagesFromThread(threadId: ThreadId, startIndex: Long, pageSize: Long): List<MessageItem>
}