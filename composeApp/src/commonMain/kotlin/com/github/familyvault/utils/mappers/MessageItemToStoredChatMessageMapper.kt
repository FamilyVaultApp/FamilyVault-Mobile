package com.github.familyvault.utils.mappers

import com.github.familyvault.backend.models.ThreadMessageItem
import com.github.familyvault.database.chatMessage.StoredChatMessage
import com.github.familyvault.models.enums.ChatMessageContentType
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

object MessageItemToStoredChatMessageMapper {
    fun map(msg: ThreadMessageItem, chatThreadId: String): StoredChatMessage = StoredChatMessage(
        id = msg.messageId,
        chatThreadId = chatThreadId,
        authorId = msg.authorId,
        authorPublicKey = msg.authorPublicKey,
        content = msg.messageContent ?: "",
        createDate = msg.createDate.toInstant(TimeZone.UTC).toEpochMilliseconds(),
        type = ChatMessageContentType.valueOf(msg.privateMeta.messageType)
    )
}