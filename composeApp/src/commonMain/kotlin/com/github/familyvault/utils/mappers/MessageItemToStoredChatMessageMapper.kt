package com.github.familyvault.utils.mappers

import com.github.familyvault.backend.models.MessageItem
import com.github.familyvault.database.chatMessage.StoredChatMessage
import com.github.familyvault.models.enums.ChatMessageType
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

object MessageItemToStoredChatMessageMapper {
    fun map(msg: MessageItem, chatThreadId: String): StoredChatMessage = StoredChatMessage(
        id = msg.messageId,
        chatThreadId = chatThreadId,
        authorId = msg.authorId,
        authorPublicKey = msg.authorPublicKey,
        content = msg.messageContent ?: "",
        createDate = msg.createDate.toInstant(TimeZone.UTC).toEpochMilliseconds(),
        type = ChatMessageType.valueOf(msg.privateMeta.messageType)
    )
}