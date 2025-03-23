package com.github.familyvault.models

import kotlinx.serialization.Serializable

@Serializable
data class JoinStatusInfo(
    val contextId: String?,
    val error: String?
)