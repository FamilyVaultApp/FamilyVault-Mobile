package com.github.familyvault.utils.mappers

import com.github.familyvault.models.chat.ChatMessage
import com.github.familyvault.backend.models.MessageItem

object MessageItemToChatMessageMapper {
    fun map(msg: MessageItem, userId: String): ChatMessage = ChatMessage(
        id = msg.messageId,
        senderPubKey = msg.authorPublicKey,
        message = msg.messageContent ?: "",
        senderId = msg.authorId,
        sendDate = msg.createDate,
        isAuthor = msg.authorPublicKey.compareTo(userId) == 0
    )
}