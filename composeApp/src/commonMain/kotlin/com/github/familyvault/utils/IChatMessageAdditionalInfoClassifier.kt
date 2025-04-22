package com.github.familyvault.utils

import com.github.familyvault.models.chat.ChatMessage
import com.github.familyvault.models.enums.chat.ChatMessageAdditionalInfo

interface IChatMessageAdditionalInfoClassifier {
    fun classify(
        message: ChatMessage,
        prevMessage: ChatMessage?,
        nextMessage: ChatMessage?
    ): ChatMessageAdditionalInfo
}