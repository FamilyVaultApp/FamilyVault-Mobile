package com.github.familyvault.models.chat

data class MessageItem(
    val messageContent: String?,
    val authorPublicKey: String,
    val decodedData: String,
    val decodedPublicMeta: MessagePublicMeta
)