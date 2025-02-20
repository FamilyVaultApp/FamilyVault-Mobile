package com.github.familyvault.services

interface IFamilyGroupManagerService {
    suspend fun createFamilyGroup(name: String, description: String? = null)
}