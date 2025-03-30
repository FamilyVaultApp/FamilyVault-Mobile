package com.github.familyvault.models.chat

data class ChatMessage(val sender: String, val message: String) {
    val messageShortPreview
        get() = if (message.length > 20) message.substring(0, 20) + "..." else message
}