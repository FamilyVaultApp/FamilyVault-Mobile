package com.github.familyvault.backend.requests

import com.github.familyvault.models.enums.FamilyGroupMemberPermissionGroup
import kotlinx.serialization.Serializable

@Serializable
data class AddMemberToFamilyGroupRequest (val contextId: String, val userId: String, val userPubKey: String, val role: Int = FamilyGroupMemberPermissionGroup.Member.value)