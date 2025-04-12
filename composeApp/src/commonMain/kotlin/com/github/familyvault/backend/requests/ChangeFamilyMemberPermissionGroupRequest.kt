package com.github.familyvault.backend.requests

import com.github.familyvault.models.enums.FamilyGroupMemberPermissionGroup
import kotlinx.serialization.Serializable

@Serializable
data class ChangeFamilyMemberPermissionGroupRequest(val contextId: String, val userId: String, val role: FamilyGroupMemberPermissionGroup) :
    FamilyVaultBackendRequest()