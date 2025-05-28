package com.github.familyvault.utils

import com.github.familyvault.models.chat.ChatMessage
import com.github.familyvault.models.enums.chat.ChatMessageAdditionalInfo

object ChatMessageAdditionalInfoClassifier : IChatMessageAdditionalInfoClassifier {
    override fun classify(
        message: ChatMessage,
        prevMessage: ChatMessage?,
        nextMessage: ChatMessage?
    ): ChatMessageAdditionalInfo {
        if (nextMessage == null) {
            if (prevMessage != null && senderIsDifferent(message, prevMessage)) {
                return ChatMessageAdditionalInfo.FULL
            }
            return ChatMessageAdditionalInfo.SENDER_NAME
        }

        if (prevMessage == null) {
            if (senderIsDifferent(nextMessage, message)) {
                return ChatMessageAdditionalInfo.FULL
            }
            return ChatMessageAdditionalInfo.SEND_DATE
        }

        if (
            senderIsDifferent(nextMessage, message) &&
            senderIsDifferent(message, prevMessage)
        ) {
            return ChatMessageAdditionalInfo.FULL
        }

        if (senderIsDifferent(nextMessage, message) && !senderIsDifferent(message, prevMessage)) {
            return ChatMessageAdditionalInfo.SENDER_NAME
        }

        if (!senderIsDifferent(nextMessage, message) && senderIsDifferent(message, prevMessage)) {
            return ChatMessageAdditionalInfo.SEND_DATE
        }

        if (!senderIsDifferent(nextMessage, message) && !senderIsDifferent(message, prevMessage)) {
            return ChatMessageAdditionalInfo.EMPTY
        }

        return ChatMessageAdditionalInfo.FULL
    }


    private inline fun senderIsDifferent(firstMessage: ChatMessage, secondMessage: ChatMessage) =
        firstMessage.senderPubKey != secondMessage.senderPubKey

}