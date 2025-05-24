package com.github.familyvault.services

import com.github.familyvault.database.familyGroupCredential.FamilyGroupCredential
import com.github.familyvault.models.FamilyGroup

interface ISavedFamilyGroupsService {
    suspend fun getAllSavedFamilyGroups(): List<FamilyGroup>
    suspend fun getSavedFamilyGroupCredential(
        contextId: String,
        memberPublicKey: String
    ): FamilyGroupCredential

    suspend fun changeDefaultFamilyGroupCredential(contextId: String, memberPublicKey: String)
    suspend fun changeFamilyGroupName(
        contextId: String,
        familyName: String
    )
}