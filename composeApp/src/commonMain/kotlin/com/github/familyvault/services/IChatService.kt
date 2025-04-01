package com.github.familyvault.services

import com.github.familyvault.models.chat.MessageItem
import com.github.familyvault.models.chat.ThreadId
import com.github.familyvault.models.chat.ThreadItem

interface IChatService {
    suspend fun createThread(threadTag: String, threadName: String?): ThreadId
    fun retrieveAllThreads(): List<ThreadItem>
    fun sendMessage(threadId: ThreadId, messageContent: String, respondToMessageId: String)
    fun retrieveMessagesFromThread(threadId: ThreadId): List<MessageItem>
}