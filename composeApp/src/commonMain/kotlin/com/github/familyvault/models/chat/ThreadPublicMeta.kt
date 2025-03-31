package com.github.familyvault.models.chat

import kotlinx.serialization.Serializable

@Serializable
data class ThreadPublicMeta(val tags: List<String>)