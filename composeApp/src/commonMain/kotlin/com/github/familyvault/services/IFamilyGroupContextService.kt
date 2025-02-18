package com.github.familyvault.services

interface IFamilyGroupContextService {
    fun setCurrentFamilyGroupId(contextId: String)
    fun getCurrentFamilyGroupId(): String
}