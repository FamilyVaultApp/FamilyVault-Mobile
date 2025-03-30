package com.github.familyvault.services

import com.github.familyvault.AppConfig
import com.github.familyvault.backend.client.FamilyVaultBackendClient
import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.backend.requests.AddMemberToFamilyGroupRequest
import com.github.familyvault.backend.requests.CreateFamilyGroupRequest
import com.github.familyvault.backend.requests.ListMembersFromFamilyGroupRequest
import com.github.familyvault.backend.requests.RenameFamilyGroupRequest
import com.github.familyvault.models.FamilyMember
import com.github.familyvault.models.PublicPrivateKeyPair
import com.github.familyvault.models.enums.FamilyGroupMemberPermissionGroup
import com.github.familyvault.repositories.IFamilyGroupCredentialsRepository

class FamilyGroupService(
    private val privMxClient: IPrivMxClient,
    private val familyGroupSessionService: IFamilyGroupSessionService,
    private val familyGroupCredentialsRepository: IFamilyGroupCredentialsRepository
) : IFamilyGroupService {
    private val familyVaultBackendProxy = FamilyVaultBackendClient()

    override suspend fun createFamilyGroupAndAssign(
        firstname: String,
        surname: String,
        secret: String,
        familyGroupName: String,
        familyGroupDescription: String?
    ) {
        val solutionId = familyVaultBackendProxy.getSolutionId().solutionId
        val pairOfKeys = privMxClient.generatePairOfPrivateAndPublicKey(secret, AppConfig.SALT)
        val username = "$firstname $surname"

        val contextId = familyVaultBackendProxy.createFamilyGroup(
            CreateFamilyGroupRequest(familyGroupName, familyGroupDescription ?: "Test description")
        ).contextId
        familyVaultBackendProxy.addMemberToFamilyGroup(
            AddMemberToFamilyGroupRequest(
                contextId,
                username,
                pairOfKeys.publicKey,
                FamilyGroupMemberPermissionGroup.Guardian
            )
        )
        familyGroupSessionService.assignSession(
            AppConfig.PRIVMX_BRIDGE_URL, solutionId, contextId, pairOfKeys, familyGroupName
        )
        familyGroupSessionService.connect()
        familyGroupCredentialsRepository.addDefaultCredential(
            familyGroupName, solutionId, contextId, pairOfKeys
        )
    }

    override suspend fun joinFamilyGroupAndAssign(
        firstname: String, surname: String, keyPair: PublicPrivateKeyPair, contextId: String
    ) {
        val solutionId = familyVaultBackendProxy.getSolutionId().solutionId
        val familyGroupName = familyGroupCredentialsRepository.getDefaultCredential()!!.name

        familyGroupSessionService.assignSession(
            AppConfig.PRIVMX_BRIDGE_URL, solutionId, contextId, keyPair, familyGroupName
        )
        familyGroupSessionService.connect()
        familyGroupCredentialsRepository.addDefaultCredential(
            contextId, solutionId, contextId, keyPair // TODO: Dodać tutaj jako name, nazwe grupy rodzinnej
        )
    }

    override suspend fun assignDefaultStoredFamilyGroup(): Boolean {
        val credential = familyGroupCredentialsRepository.getDefaultCredential()

        if (credential != null) {
            familyGroupSessionService.assignSession(
                AppConfig.PRIVMX_BRIDGE_URL,
                credential.solutionId,
                credential.contextId,
                PublicPrivateKeyPair(credential.publicKey, credential.privateKey),
                credential.name
            )
            familyGroupSessionService.connect()
            return true
        }
        return false
    }

    override suspend fun addMemberToFamilyGroup(
        contextId: String, userId: String, userPubKey: String
    ) {
        familyVaultBackendProxy.addMemberToFamilyGroup(
            AddMemberToFamilyGroupRequest(
                contextId, userId, userPubKey
            )
        )
    }

    override suspend fun retrieveFamilyGroupMembersList(): List<FamilyMember> {
        val contextId = familyGroupSessionService.getContextId()

        return familyVaultBackendProxy.listMembersOfFamilyGroup(
            ListMembersFromFamilyGroupRequest(
                contextId
            )
        ).members
    }

    override suspend fun renameFamilyGroup(
        contextId: String, name: String
    ) {
        familyVaultBackendProxy.renameFamilyGroup(
            RenameFamilyGroupRequest(
                contextId, name
            )
        )
    }
}