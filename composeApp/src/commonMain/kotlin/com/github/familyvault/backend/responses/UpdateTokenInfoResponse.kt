package com.github.familyvault.backend.responses

import com.github.familyvault.models.FamilyMemberJoinStatus
import kotlinx.serialization.Serializable

@Serializable
data class UpdateTokenInfoResponse(val familyMemberJoinStatus: FamilyMemberJoinStatus)