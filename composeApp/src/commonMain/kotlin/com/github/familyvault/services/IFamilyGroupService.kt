package com.github.familyvault.services

import com.github.familyvault.models.FamilyMember
import com.github.familyvault.models.FamilyMemberJoinStatus
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
        contextId: String
    )

    suspend fun assignDefaultStoredFamilyGroup(): Boolean

    suspend fun retrieveFamilyGroupMembersList(): List<FamilyMember>

    suspend fun generateJoinToken(): FamilyMemberJoinStatus

    suspend fun getTokenStatus(token: String): FamilyMemberJoinStatus

    suspend fun updateTokenStatus(token: String, status: Int): FamilyMemberJoinStatus

    suspend fun updateTokenInfo(token: String, contextId: String): FamilyMemberJoinStatus

    suspend fun addMemberToFamilyGroup(contextId: String, userId: String, userPubKey: String)
}