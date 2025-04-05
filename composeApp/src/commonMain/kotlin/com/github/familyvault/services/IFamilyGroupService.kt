package com.github.familyvault.services

import com.github.familyvault.models.FamilyMember
import com.github.familyvault.models.PublicEncryptedPrivateKeyPair

interface IFamilyGroupService {
    suspend fun createFamilyGroupAndAssign(
        firstname: String,
        surname: String,
        password: String,
        familyGroupName: String,
        familyGroupDescription: String? = null
    )

    suspend fun joinFamilyGroupAndAssign(
        firstname: String,
        surname: String,
        encryptedPassword: String,
        keyPair: PublicEncryptedPrivateKeyPair,
        contextId: String
    )

    suspend fun assignDefaultStoredFamilyGroup(): Boolean

    suspend fun retrieveFamilyGroupMembersList(): List<FamilyMember>

    suspend fun addMemberToFamilyGroup(contextId: String, userId: String, userPubKey: String)

    suspend fun renameCurrentFamilyGroup(name: String)

    suspend fun refreshCurrentFamilyGroupName()

    suspend fun removeMemberFromCurrentFamilyGroup(userPubKey: String)
}