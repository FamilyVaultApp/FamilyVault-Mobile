package com.github.familyvault.models.chat

// TODO: Wyrzucenie MessageItem na rzecz ChatMessage
data class MessageItem(
    val messageContent: String?,
    val authorPublicKey: String,
    val decodedData: String,
    val decodedPublicMeta: MessagePublicMeta
)