package com.github.familyvault.utils.mappers

import com.github.familyvault.models.chat.MessageItem
import com.simplito.java.privmx_endpoint.model.Message

object PrivMxMessageToMessageItemMapper {
    fun map(msg: Message): MessageItem = MessageItem(
        messageContent = msg.data.decodeToString(),
        authorPublicKey = msg.authorPubKey,
    )
}
