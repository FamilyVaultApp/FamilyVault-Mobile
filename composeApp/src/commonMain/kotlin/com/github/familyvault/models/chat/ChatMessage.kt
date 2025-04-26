package com.github.familyvault.models.chat

import com.github.familyvault.models.enums.chat.ChatMessageContentType
import kotlinx.datetime.LocalDateTime

data class ChatMessage(
    val id: String,
    val message: String,
    val senderId: String,
    val senderPubKey: String,
    val sendDate: LocalDateTime,
    val isAuthor: Boolean,
    val type: ChatMessageContentType
) {
    override fun equals(other: Any?): Boolean {
        if (other is ChatMessage) {
            return id == other.id
        }
        return false
    }

    override fun hashCode(): Int {
        return this::class.hashCode()
    }
}