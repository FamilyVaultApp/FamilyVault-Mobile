package com.github.familyvault.services

import com.github.familyvault.backend.FamilyVaultBackendProxy
import com.github.familyvault.backend.requests.CreateFamilyGroupRequest

class FamilyGroupManagerService(private val familyGroupContextService: FamilyGroupContextService) : IFamilyGroupManagerService {
    private val familyVaultBackendProxy = FamilyVaultBackendProxy()

    override suspend fun createFamilyGroup(name: String, description: String?) {
        val response = familyVaultBackendProxy.createFamilyGroup(
            CreateFamilyGroupRequest(name, description ?: "Test description")
        )

        familyGroupContextService.setCurrentFamilyGroupId(response.contextId)
    }
}