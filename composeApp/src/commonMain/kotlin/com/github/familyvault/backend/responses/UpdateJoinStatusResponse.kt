package com.github.familyvault.backend.responses

import com.github.familyvault.models.JoinStatus
import kotlinx.serialization.Serializable

@Serializable
data class UpdateJoinStatusResponse(val joinStatus: JoinStatus) :
    FamilyVaultBackendResponse()