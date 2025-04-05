package com.github.familyvault.backend.responses

import kotlinx.serialization.Serializable

@Serializable
data class GetFamilyGroupNameResponse(val familyGroupName: String) : FamilyVaultBackendResponse()
