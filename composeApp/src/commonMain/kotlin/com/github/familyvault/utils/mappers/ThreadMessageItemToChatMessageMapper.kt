package com.github.familyvault.utils.mappers

import com.github.familyvault.backend.models.ThreadMessageItem
import com.github.familyvault.models.chat.ChatMessage
import com.github.familyvault.models.enums.chat.ChatMessageContentType
import kotlinx.serialization.json.Json

object ThreadMessageItemToChatMessageMapper {
    fun map(msg: ThreadMessageItem, userId: String): ChatMessage = ChatMessage(
        id = msg.messageId,
        senderPubKey = msg.authorPublicKey,
        message = msg.messageContent ?: "",
        senderId = Json.decodeFromString(msg.authorId),
        sendDate = msg.createDate,
        isAuthor = msg.authorPublicKey.compareTo(userId) == 0,
        type = ChatMessageContentType.valueOf(msg.privateMeta.messageType)
    )
}