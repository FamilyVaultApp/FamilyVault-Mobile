package com.github.familyvault.services

import com.github.familyvault.models.chat.MessageItem
import com.github.familyvault.models.chat.ThreadId

interface IChatService {
    suspend fun createThread(threadTag: String, threadName: String?): ThreadId
    fun retrieveAllThreadIds(): List<ThreadId>
    fun sendMessage(threadId: ThreadId, messageContent: String, respondToMessageId: String)
    fun retrieveMessagesFromThread(threadId: ThreadId): List<MessageItem>
}