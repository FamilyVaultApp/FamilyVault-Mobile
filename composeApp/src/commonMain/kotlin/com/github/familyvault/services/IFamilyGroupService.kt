package com.github.familyvault.services

import com.github.familyvault.models.FamilyMember
import com.github.familyvault.models.PublicEncryptedPrivateKeyPair
import com.github.familyvault.models.SelfHostedConnectionInfo
import com.github.familyvault.models.enums.ConnectionStatus

interface IFamilyGroupService {
    suspend fun createFamilyGroupAndAssign(
        firstname: String,
        surname: String,
        password: String,
        familyGroupName: String,
        familyGroupDescription: String? = null,
        connectionInfo: SelfHostedConnectionInfo? = null
    )

    suspend fun joinFamilyGroupAndAssign(
        firstname: String,
        surname: String,
        encryptedPassword: String,
        keyPair: PublicEncryptedPrivateKeyPair,
        contextId: String,
        connectionInfo: SelfHostedConnectionInfo? = null
    )

    suspend fun assignDefaultStoredFamilyGroup(): ConnectionStatus

    suspend fun retrieveFamilyGroupMembersList(): List<FamilyMember>

    suspend fun addMemberToFamilyGroup(contextId: String, userId: String, userPubKey: String)

    suspend fun renameCurrentFamilyGroup(name: String)

    suspend fun refreshCurrentFamilyGroupName()

    suspend fun retrieveFamilyGroupMembersWithoutMeList(): List<FamilyMember>

    suspend fun retrieveMyFamilyMemberData(): FamilyMember

    suspend fun retrieveFamilyMemberDataByPublicKey(publicKey: String): FamilyMember

    suspend fun removeMemberFromCurrentFamilyGroup(userPubKey: String)
}