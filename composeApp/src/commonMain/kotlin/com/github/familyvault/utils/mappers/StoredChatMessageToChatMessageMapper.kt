package com.github.familyvault.utils.mappers

import com.github.familyvault.database.chatMessage.StoredChatMessage
import com.github.familyvault.models.chat.ChatMessage
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object StoredChatMessageToChatMessageMapper {
    fun map(storedChatMessage: StoredChatMessage, userId: String): ChatMessage =
        ChatMessage(
            id = storedChatMessage.id,
            message = storedChatMessage.content,
            senderId = storedChatMessage.authorId,
            senderPubKey = storedChatMessage.authorPublicKey,
            sendDate = Instant.fromEpochMilliseconds(storedChatMessage.createDate).toLocalDateTime(
                TimeZone.UTC
            ),
            isAuthor = storedChatMessage.authorPublicKey.compareTo(userId) == 0
        )
}
