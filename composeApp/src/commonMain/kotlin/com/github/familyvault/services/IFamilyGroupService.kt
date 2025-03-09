package com.github.familyvault.services

import com.github.familyvault.models.FamilyMember

interface IFamilyGroupService {
    suspend fun createFamilyGroupAndAssign(
        firstname: String,
        surname: String,
        secret: String,
        familyGroupName: String,
        familyGroupDescription: String? = null
    )
    suspend fun assignDefaultStoredFamilyGroup(): Boolean

    suspend fun retrieveFamilyGroupMembersList(): MutableList<FamilyMember>
}