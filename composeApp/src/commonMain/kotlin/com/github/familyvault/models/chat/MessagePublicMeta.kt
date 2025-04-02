package com.github.familyvault.models.chat

import kotlinx.serialization.Serializable

@Serializable
data class MessagePublicMeta(
    val responseTo: String
)