package com.github.familyvault.models.chat

import com.simplito.java.privmx_endpoint.model.Thread

data class ThreadItem(
    val thread: Thread,
    val decodedPrivateMeta: String,
    val decodedPublicMeta: String
)