package com.github.familyvault.backend.requests

import kotlinx.serialization.Serializable

@Serializable
data class GetJoinStatusRequest(val token: String) : FamilyVaultBackendRequest() {
    override fun toParameters(): Map<String, String> {
        return mapOf(
            "token" to token,
        )
    }
}