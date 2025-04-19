package com.github.familyvault.services

import com.github.familyvault.models.FamilyGroup

interface ISavedFamilyGroupsService {
    suspend fun getAllSavedFamilyGroups(): List<FamilyGroup>
}