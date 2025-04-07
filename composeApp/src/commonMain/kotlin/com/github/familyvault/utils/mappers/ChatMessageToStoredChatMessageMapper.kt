package com.github.familyvault.utils.mappers

import com.github.familyvault.database.chatMessage.StoredChatMessage
import com.github.familyvault.models.chat.ChatMessage
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

object ChatMessageToStoredChatMessageMapper {
    fun map(chatMessage: ChatMessage, chatThreadId: String) = StoredChatMessage(
        id = chatMessage.id,
        chatThreadId = chatThreadId,
        authorId = chatMessage.senderId,
        authorPublicKey = chatMessage.senderPubKey,
        content = chatMessage.message,
        createDate = chatMessage.sendDate.toInstant(TimeZone.UTC).toEpochMilliseconds()
    )
}