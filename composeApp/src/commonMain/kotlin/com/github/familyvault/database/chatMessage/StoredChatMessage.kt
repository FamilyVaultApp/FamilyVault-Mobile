package com.github.familyvault.database.chatMessage

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.familyvault.models.enums.chat.ChatMessageContentType

@Entity
data class StoredChatMessage(
    @PrimaryKey
    val id: String,
    val chatThreadId: String,
    val authorId: String,
    val authorPublicKey: String,
    val content: String,
    val createDate: Long,
    val type: ChatMessageContentType
)