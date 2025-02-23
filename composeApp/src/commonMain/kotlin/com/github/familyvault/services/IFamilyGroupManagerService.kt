package com.github.familyvault.services

interface IFamilyGroupManagerService {
    suspend fun createFamilyGroup(
        firstname: String,
        surname: String,
        secret: String,
        familyGroupName: String,
        familyGroupDescription: String? = null
    )
}