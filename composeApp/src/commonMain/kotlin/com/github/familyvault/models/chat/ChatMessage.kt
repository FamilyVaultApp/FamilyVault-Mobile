package com.github.familyvault.models.chat

import kotlinx.datetime.LocalDateTime

data class ChatMessage(
    val id: String,
    val message: String,
    val senderId: String,
    val senderPubKey: String,
    val sendDate: LocalDateTime,
    val isAuthor: Boolean
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