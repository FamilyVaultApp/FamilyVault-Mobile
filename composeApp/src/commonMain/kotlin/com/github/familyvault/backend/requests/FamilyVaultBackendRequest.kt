package com.github.familyvault.backend.requests

abstract class FamilyVaultBackendRequest {
    open fun toParameters(): Map<String, String> {
        throw NotImplementedError("toParameters is not implemented in this request")
    }
}