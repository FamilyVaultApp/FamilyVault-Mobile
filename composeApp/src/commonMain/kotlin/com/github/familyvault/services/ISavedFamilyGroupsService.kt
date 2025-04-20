package com.github.familyvault.services

import com.github.familyvault.database.familyGroupCredential.FamilyGroupCredential
import com.github.familyvault.models.FamilyGroup

interface ISavedFamilyGroupsService {
    suspend fun getAllSavedFamilyGroups(): List<FamilyGroup>
    suspend fun getSavedFamilyGroupCredentialByContextId(contextId: String): FamilyGroupCredential
    suspend fun changeDefaultFamilyGroupCredential(contextId: String)
}