package com.github.familyvault.models.enums.chat

enum class ChatMessageAdditionalInfo {
    SENDER_NAME,
    SEND_DATE,
    FULL,
    EMPTY,
}

fun ChatMessageAdditionalInfo.shouldRenderSenderName(): Boolean =
    this == ChatMessageAdditionalInfo.SENDER_NAME || this == ChatMessageAdditionalInfo.FULL

fun ChatMessageAdditionalInfo.shouldRenderMessageSendDate(): Boolean =
    this == ChatMessageAdditionalInfo.SEND_DATE || this == ChatMessageAdditionalInfo.FULL
