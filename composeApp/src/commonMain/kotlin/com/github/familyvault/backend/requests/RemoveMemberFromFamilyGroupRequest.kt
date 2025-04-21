package com.github.familyvault.backend.requests

import kotlinx.serialization.Serializable

@Serializable
data class RemoveMemberFromFamilyGroupRequest(
    val contextId: String,
    val userPubKey: String
) : FamilyVaultBackendRequest()