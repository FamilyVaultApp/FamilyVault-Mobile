package com.github.familyvault.backend.responses

import com.github.familyvault.models.FamilyMemberJoinStatus
import kotlinx.serialization.Serializable

@Serializable
data class GetTokenStatusResponse(val familyMemberJoinStatus: FamilyMemberJoinStatus)