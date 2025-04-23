package com.github.familyvault.utils

import com.github.familyvault.models.chat.ChatMessage
import com.github.familyvault.models.enums.chat.ChatMessageAdditionalInfo

object ChatMessageAdditionalInfoClassifier : IChatMessageAdditionalInfoClassifier {
    override fun classify(
        message: ChatMessage,
        prevMessage: ChatMessage?,
        nextMessage: ChatMessage?
    ): ChatMessageAdditionalInfo {
        if (prevMessage == null) {
            if (nextMessage != null && senderIsDifferent(message, nextMessage)) {
                return ChatMessageAdditionalInfo.FULL
            }
            return ChatMessageAdditionalInfo.SENDER_NAME
        }

        if (nextMessage == null) {
            if (senderIsDifferent(prevMessage, message)) {
                return ChatMessageAdditionalInfo.FULL
            }
            return ChatMessageAdditionalInfo.SEND_DATE
        }

        if (
            senderIsDifferent(prevMessage, message) &&
            senderIsDifferent(message, nextMessage)
        ) {
            return ChatMessageAdditionalInfo.FULL
        }

        if (senderIsDifferent(prevMessage, message) && !senderIsDifferent(message, nextMessage)) {
            return ChatMessageAdditionalInfo.SENDER_NAME
        }

        if (!senderIsDifferent(prevMessage, message) && senderIsDifferent(message, nextMessage)) {
            return ChatMessageAdditionalInfo.SEND_DATE
        }

        if (!senderIsDifferent(prevMessage, message) && !senderIsDifferent(message, nextMessage)) {
            return ChatMessageAdditionalInfo.EMPTY
        }

        return ChatMessageAdditionalInfo.FULL
    }


    private inline fun senderIsDifferent(firstMessage: ChatMessage, secondMessage: ChatMessage) =
        firstMessage.senderPubKey != secondMessage.senderPubKey

}