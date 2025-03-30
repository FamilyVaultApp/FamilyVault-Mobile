package com.github.familyvault.services

import com.github.familyvault.models.FamilyMember
import com.github.familyvault.models.PublicPrivateKeyPair

interface IFamilyGroupService {
    suspend fun createFamilyGroupAndAssign(
        firstname: String,
        surname: String,
        secret: String,
        familyGroupName: String,
        familyGroupDescription: String? = null
    )

    suspend fun joinFamilyGroupAndAssign(
        firstname: String,
        surname: String,
        keyPair: PublicPrivateKeyPair,
        contextId: String,
    )

    suspend fun assignDefaultStoredFamilyGroup(): Boolean

    suspend fun retrieveFamilyGroupMembersList(): List<FamilyMember>

    suspend fun addMemberToFamilyGroup(contextId: String, userId: String, userPubKey: String)

    suspend fun renameFamilyGroup(contextId: String, name: String?)
}