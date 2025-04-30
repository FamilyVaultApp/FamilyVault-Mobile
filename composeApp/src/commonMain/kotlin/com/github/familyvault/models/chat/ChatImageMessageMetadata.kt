package com.github.familyvault.models.chat

import kotlinx.serialization.Serializable

@Serializable
data class ChatImageMessageMetadata(val storeFileId: String, val height: Int, val width: Int)