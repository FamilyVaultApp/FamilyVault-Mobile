package com.github.familyvault.backend.responses

import com.github.familyvault.models.JoinStatus
import kotlinx.serialization.Serializable

@Serializable
data class GetJoinStatusResponse(val joinStatus: JoinStatus) :
    FamilyVaultBackendResponse()