package com.github.familyvault.database.chatMessage

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface StoredChatMessageDao {
    @Query("SELECT * FROM StoredChatMessage WHERE chatThreadId = :chatThreadId ORDER BY createDate DESC LIMIT :take OFFSET :skip")
    suspend fun getMessages(chatThreadId: String, skip: Int, take: Int): List<StoredChatMessage>

    @Query("SELECT EXISTS(SELECT 1 FROM StoredChatMessage WHERE id = :id)")
    suspend fun isMessageStored(id: String): Boolean

    @Upsert
    suspend fun upsert(chatMessage: StoredChatMessage)
}