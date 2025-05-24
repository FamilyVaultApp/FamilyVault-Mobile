package com.github.familyvault.services

import com.github.familyvault.database.familyGroupCredential.FamilyGroupCredential
import com.github.familyvault.models.FamilyGroup
import com.github.familyvault.repositories.IFamilyGroupCredentialsRepository
import com.github.familyvault.utils.mappers.FamilyGroupCredentialToFamilyGroupMapper

class SavedFamilyGroupsService(
    private val familyGroupCredentialsRepository: IFamilyGroupCredentialsRepository
) : ISavedFamilyGroupsService {
    override suspend fun getAllSavedFamilyGroups(): List<FamilyGroup> {
        val credentials = familyGroupCredentialsRepository.getAllCredentials()
        return credentials.map { FamilyGroupCredentialToFamilyGroupMapper.map(it) }
    }

    override suspend fun getSavedFamilyGroupCredential(
        contextId: String,
        memberPublicKey: String
    ): FamilyGroupCredential {
        return familyGroupCredentialsRepository.getCredential(
            contextId,
            memberPublicKey
        )
    }

    override suspend fun changeDefaultFamilyGroupCredential(
        contextId: String,
        memberPublicKey: String
    ) {
        familyGroupCredentialsRepository.setDefaultCredential(contextId, memberPublicKey)
    }

    override suspend fun changeFamilyGroupName(
        contextId: String,
        familyName: String
    ) {
        familyGroupCredentialsRepository.updateCredentialFamilyGroupName(
            contextId,
            familyName
        )
    }
}