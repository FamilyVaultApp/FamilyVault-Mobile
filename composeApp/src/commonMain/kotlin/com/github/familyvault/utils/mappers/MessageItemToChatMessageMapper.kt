package com.github.familyvault.utils.mappers

import com.github.familyvault.models.chat.ChatMessage
import com.github.familyvault.models.chat.MessageItem

object MessageItemToChatMessageMapper {
    inline fun map(msg: MessageItem, userId: String): ChatMessage = ChatMessage(
        sender = msg.authorPublicKey,
        message = msg.messageContent ?: "",
        isAuthor = msg.authorPublicKey.compareTo(userId) == 0
    )
}