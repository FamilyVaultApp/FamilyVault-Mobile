package com.github.familyvault.backend.requests

import kotlinx.serialization.Serializable

@Serializable
data class GetFamilyGroupNameRequest (
    val contextId: String
) : FamilyVaultBackendRequest()