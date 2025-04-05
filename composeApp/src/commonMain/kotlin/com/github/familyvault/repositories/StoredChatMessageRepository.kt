package com.github.familyvault.repositories

import com.github.familyvault.AppConfig
import com.github.familyvault.database.AppDatabase
import com.github.familyvault.database.chatMessage.StoredChatMessage

class StoredChatMessageRepository(private val appDatabase: AppDatabase) :
    IStoredChatMessageRepository {

    override suspend fun getStoredChatMessagesPage(
        chatThreadId: String,
        page: Int
    ): List<StoredChatMessage> {
        val storedChatMessageDao = getStoredChatMessageDao()

        return storedChatMessageDao.getMessages(
            chatThreadId,
            take = AppConfig.CHAT_MESSAGES_PER_PAGE,
            skip = AppConfig.CHAT_MESSAGES_PER_PAGE * page
        )
    }

    override suspend fun addNewStoredChatMessage(message: StoredChatMessage) {
        val storedChatMessageDao = getStoredChatMessageDao()

        storedChatMessageDao.upsert(message)
    }

    override suspend fun addNewStoredChatMessages(messages: List<StoredChatMessage>) {
        val storedChatMessageDao = getStoredChatMessageDao()

        for (message in messages) {
            storedChatMessageDao.upsert(message)
        }
    }

    override suspend fun isChatMessageInRepositoryById(id: String): Boolean {
        val storedChatMessage = getStoredChatMessageDao()

        return storedChatMessage.isMessageStored(id)
    }

    private fun getStoredChatMessageDao() = appDatabase.storedChatMessageDao()
}