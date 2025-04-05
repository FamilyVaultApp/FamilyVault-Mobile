package com.github.familyvault.services

import com.github.familyvault.models.FamilyMember
import com.github.familyvault.models.chat.ChatMessage
import com.github.familyvault.models.chat.ChatThread

interface IChatService {
    suspend fun createGroupChat(name: String, members: List<FamilyMember>): ChatThread
    fun retrieveAllChatThreads(): List<ChatThread>
    fun sendMessage(chatThreadId: String, messageContent: String, respondToMessageId: String)
    fun retrieveMessages(chatThreadId: String): List<ChatMessage>
    fun retrieveLastMessage(chatThreadId: String): ChatMessage?
}