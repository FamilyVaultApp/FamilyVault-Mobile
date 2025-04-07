package com.github.familyvault.repositories

import com.github.familyvault.database.chatMessage.StoredChatMessage

interface IStoredChatMessageRepository {
    suspend fun getStoredChatMessagesPage(chatThreadId: String, page: Int): List<StoredChatMessage>
    suspend fun addNewStoredChatMessage(message: StoredChatMessage)
    suspend fun addNewStoredChatMessages(messages: List<StoredChatMessage>)
    suspend fun isChatMessageInRepositoryById(id: String): Boolean
}