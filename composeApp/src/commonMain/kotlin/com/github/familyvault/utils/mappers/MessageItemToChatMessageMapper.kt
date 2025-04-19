package com.github.familyvault.utils.mappers

import com.github.familyvault.backend.models.ThreadMessageItem
import com.github.familyvault.models.chat.ChatMessage
import com.github.familyvault.models.enums.ChatMessageContentType

object MessageItemToChatMessageMapper {
    fun map(msg: ThreadMessageItem, userId: String): ChatMessage = ChatMessage(
        id = msg.messageId,
        senderPubKey = msg.authorPublicKey,
        message = msg.messageContent ?: "",
        senderId = msg.authorId,
        sendDate = msg.createDate,
        isAuthor = msg.authorPublicKey.compareTo(userId) == 0,
        type = ChatMessageContentType.valueOf(msg.privateMeta.messageType)
    )
}