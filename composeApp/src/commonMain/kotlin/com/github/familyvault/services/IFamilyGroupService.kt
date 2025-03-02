package com.github.familyvault.services

interface IFamilyGroupService {
    suspend fun createFamilyGroupAndAssign(
        firstname: String,
        surname: String,
        secret: String,
        familyGroupName: String,
        familyGroupDescription: String? = null
    )
    suspend fun assignDefaultStoredFamilyGroup(): Boolean
}