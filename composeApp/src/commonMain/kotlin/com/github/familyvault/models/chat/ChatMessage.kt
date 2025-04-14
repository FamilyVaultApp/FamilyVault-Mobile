package com.github.familyvault.models.chat

import com.github.familyvault.models.enums.ChatMessageType
import kotlinx.datetime.LocalDateTime

data class ChatMessage(
    val id: String,
    val message: String,
    val senderId: String,
    val senderPubKey: String,
    val sendDate: LocalDateTime,
    val isAuthor: Boolean,
    val type: ChatMessageType
) {
    val messageShortPreview
        get() = if (message.length > 20) message.substring(0, 20) + "..." else message

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