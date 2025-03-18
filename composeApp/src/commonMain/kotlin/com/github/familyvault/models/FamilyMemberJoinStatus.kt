package com.github.familyvault.models

import com.github.familyvault.models.enums.JoinTokenStatus
import kotlinx.serialization.Serializable

@Serializable
data class FamilyMemberJoinStatus(
    val token: String,
    val status: JoinTokenStatus,
    val info: ContextIdInfo?
)

@Serializable
data class ContextIdInfo(
    val contextId: String
)
