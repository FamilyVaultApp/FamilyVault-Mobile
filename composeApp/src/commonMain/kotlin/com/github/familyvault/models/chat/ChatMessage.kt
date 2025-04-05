package com.github.familyvault.models.chat

data class ChatMessage(
    val id: String,
    val message: String,
    val senderId: String,
    val senderPubKey: String,
    val isAuthor: Boolean
) {
    val messageShortPreview
        get() = if (message.length > 20) message.substring(0, 20) + "..." else message
}