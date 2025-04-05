package com.github.familyvault.database.chatMessage

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface StoredChatMessageDao {
    @Query("SELECT * FROM StoredChatMessage WHERE chatThreadId = :chatThreadId LIMIT :take OFFSET :skip")
    suspend fun getMessages(chatThreadId: String, skip: Int, take: Int): List<StoredChatMessage>

    @Upsert
    suspend fun upsert(data: StoredChatMessage)
}