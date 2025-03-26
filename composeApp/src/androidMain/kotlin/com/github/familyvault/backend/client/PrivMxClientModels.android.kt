package com.github.familyvault.backend.client

import com.simplito.java.privmx_endpoint.model.Message
import com.simplito.java.privmx_endpoint.model.Thread
import kotlinx.serialization.Serializable

@Serializable
data class ThreadPublicMeta(val tags: List<String>)

data class ThreadItem(
    val thread: Thread,
    val decodedPrivateMeta: String,
    val decodedPublicMeta: String
)

@Serializable
data class MessagePublicMeta(
    val responseTo: String
)

data class MessageItem(
    val message: Message,
    val decodedData: String,
    val decodedPublicMeta: MessagePublicMeta
)