package com.github.familyvault.backend.requests

import kotlinx.serialization.Serializable

@Serializable
data class RenameFamilyGroupRequest (
    val contextId: String,
    val name: String,
) : FamilyVaultBackendRequest()