package com.github.familyvault.backend.requests

import kotlinx.serialization.Serializable

@Serializable
data class GetMemberFromFamilyGroupRequest(
    val contextId: String,
    val userId: String?,
    val publicKey: String?
) :
    FamilyVaultBackendRequest()