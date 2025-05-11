package com.github.familyvault.backend.responses

import kotlinx.serialization.Serializable

@Serializable
data class PrivMxBridgeUrlResponse(val bridgeUrl: String) : FamilyVaultBackendResponse()