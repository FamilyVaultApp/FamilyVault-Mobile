package com.github.familyvault.backend.models

import kotlinx.serialization.Serializable

@Serializable
data class ThreadMessagePrivateMeta (
    val messageType: String
)