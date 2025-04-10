package com.github.familyvault.backend.requests

import com.github.familyvault.models.enums.FamilyGroupMemberPermissionGroup
import kotlinx.serialization.Serializable

@Serializable
data class RemoveMemberFromFamilyGroupRequest(
    val contextId: String,
    val userPubKey: String
) : FamilyVaultBackendRequest()