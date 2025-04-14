package com.github.familyvault.utils.mappers

import com.github.familyvault.backend.models.MessageItem
import com.github.familyvault.backend.utils.MessageMetaDecoder
import com.github.familyvault.backend.utils.MessageMetaEncoder
import com.simplito.java.privmx_endpoint.model.Message
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object PrivMxMessageToMessageItemMapper {
    fun map(msg: Message): MessageItem = MessageItem(
        messageId = msg.info.messageId,
        messageContent = msg.data.decodeToString(),
        authorId = msg.info.author,
        authorPublicKey = msg.authorPubKey,
        createDate = Instant.fromEpochMilliseconds(msg.info.createDate)
            .toLocalDateTime(TimeZone.currentSystemDefault()),
        privateMeta = MessageMetaDecoder.decodePrivateMeta(msg.privateMeta)
    )
}
