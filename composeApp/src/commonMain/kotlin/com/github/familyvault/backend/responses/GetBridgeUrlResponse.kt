package com.github.familyvault.backend.responses

import kotlinx.serialization.Serializable

@Serializable
data class GetBridgeUrlResponse(val bridgeUrl: String) : FamilyVaultBackendResponse()