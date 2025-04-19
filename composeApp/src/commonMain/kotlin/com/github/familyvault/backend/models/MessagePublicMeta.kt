package com.github.familyvault.backend.models

import com.github.familyvault.models.enums.ChatMessageContentType
import kotlinx.serialization.Serializable

@Serializable
data class MessagePrivateMeta(val messageType: ChatMessageContentType)