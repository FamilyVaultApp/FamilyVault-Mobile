package com.github.familyvault.models

import com.github.familyvault.models.enums.JoinStatusState
import kotlinx.serialization.Serializable

@Serializable
data class JoinStatus(
    val token: String,
    val state: JoinStatusState,
    val info: JoinStatusInfo?
)