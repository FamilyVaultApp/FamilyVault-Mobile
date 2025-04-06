package com.github.familyvault.backend.models

import kotlinx.serialization.Serializable

@Serializable
data class ThreadPublicMeta(val tags: List<String>)