package com.github.familyvault.backend.responses

import com.github.familyvault.models.FamilyMemberJoinStatus
import kotlinx.serialization.Serializable

@Serializable
data class UpdateTokenStatusResponse(val joinStatus: FamilyMemberJoinStatus)