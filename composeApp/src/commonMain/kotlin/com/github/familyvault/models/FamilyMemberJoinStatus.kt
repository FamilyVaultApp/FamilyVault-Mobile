package com.github.familyvault.models

import com.github.familyvault.models.enums.JoinTokenStatus
import kotlinx.serialization.Serializable

@Serializable
data class FamilyMemberJoinStatus(
    var token: String,
    var state: JoinTokenStatus,
    val info: ContextIdInfo?
)

@Serializable
data class ContextIdInfo(
    val contextId: String,
    val error: String?
)
