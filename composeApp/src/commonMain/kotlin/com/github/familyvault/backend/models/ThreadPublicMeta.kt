package com.github.familyvault.backend.models

import kotlinx.serialization.Serializable

@Serializable
data class ThreadPublicMeta(val tag: String, val type: String)