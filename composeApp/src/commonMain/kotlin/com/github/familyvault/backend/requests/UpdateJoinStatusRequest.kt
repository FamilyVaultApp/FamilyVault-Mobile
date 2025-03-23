package com.github.familyvault.backend.requests

import com.github.familyvault.models.JoinStatusInfo
import com.github.familyvault.models.enums.JoinStatusState
import kotlinx.serialization.Serializable

@Serializable
data class UpdateJoinStatusRequest(
    val token: String,
    val state: JoinStatusState,
    val info: JoinStatusInfo?
) : FamilyVaultBackendRequest()