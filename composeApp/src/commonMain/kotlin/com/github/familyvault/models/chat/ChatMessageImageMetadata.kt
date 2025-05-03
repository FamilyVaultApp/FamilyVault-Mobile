package com.github.familyvault.models.chat

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessageImageMetadata(val storeFileId: String, val height: Int, val width: Int)
