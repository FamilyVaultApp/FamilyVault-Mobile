package com.github.familyvault.utils.mappers

import com.github.familyvault.database.chatMessage.StoredChatMessage
import com.github.familyvault.models.chat.ChatMessage
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.serialization.json.Json

object ChatMessageToStoredChatMessageMapper {
    fun map(chatMessage: ChatMessage, chatThreadId: String) = StoredChatMessage(
        id = chatMessage.id,
        chatThreadId = chatThreadId,
        authorId = Json.encodeToString(chatMessage.senderId),
        authorPublicKey = chatMessage.senderPubKey,
        content = chatMessage.message,
        createDate = chatMessage.sendDate.toInstant(TimeZone.UTC).toEpochMilliseconds(),
        type = chatMessage.type
    )
}