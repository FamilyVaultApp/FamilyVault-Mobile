package com.github.familyvault.backend.requests

import com.github.familyvault.models.enums.FamilyGroupMemberPermissionGroup
import kotlinx.serialization.Serializable

@Serializable
data class RenameFamilyGroupRequest (
    val contextId: String,
    val name: String?,
) : FamilyVaultBackendRequest()