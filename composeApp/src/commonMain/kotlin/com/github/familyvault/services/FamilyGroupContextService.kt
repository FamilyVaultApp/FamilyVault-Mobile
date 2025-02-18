package com.github.familyvault.services

class FamilyGroupContextService : IFamilyGroupContextService {
    private var familyGroupId: String? = null

    override fun setCurrentFamilyGroupId(contextId: String) {
        familyGroupId = contextId;
    }

    override fun getCurrentFamilyGroupId(): String {
        // TODO: Stworzenie customowego wyjątku
        return familyGroupId ?: throw Exception("ContextId is null")
    }
}