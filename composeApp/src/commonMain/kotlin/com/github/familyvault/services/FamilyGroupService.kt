package com.github.familyvault.services

import com.github.familyvault.AppConfig
import com.github.familyvault.backend.client.FamilyVaultBackendClient
import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.backend.requests.AddMemberToFamilyGroupRequest
import com.github.familyvault.backend.requests.CreateFamilyGroupRequest
import com.github.familyvault.backend.requests.GetFamilyGroupNameRequest
import com.github.familyvault.backend.requests.ListMembersFromFamilyGroupRequest
import com.github.familyvault.backend.requests.RemoveMemberFromFamilyGroupRequest
import com.github.familyvault.backend.requests.RenameFamilyGroupRequest
import com.github.familyvault.backend.responses.GetFamilyGroupNameResponse
import com.github.familyvault.models.FamilyMember
import com.github.familyvault.models.PublicEncryptedPrivateKeyPair
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
        password: String,
        familyGroupName: String,
        familyGroupDescription: String?
    ) {
        val solutionId = familyVaultBackendProxy.getSolutionId().solutionId
        val pairOfKeys = privMxClient.generatePairOfPrivateAndPublicKey(password)
        val encryptedPassword = privMxClient.encryptPrivateKeyPassword(password)
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
            AppConfig.PRIVMX_BRIDGE_URL, familyGroupName, solutionId, contextId, pairOfKeys
        )
        familyGroupSessionService.connect()
        familyGroupCredentialsRepository.addDefaultCredential(
            familyGroupName, solutionId, contextId, pairOfKeys, encryptedPassword
        )
    }

    override suspend fun joinFamilyGroupAndAssign(
        firstname: String,
        surname: String,
        encryptedPassword: String,
        keyPair: PublicEncryptedPrivateKeyPair,
        contextId: String
    ) {
        val solutionId = familyVaultBackendProxy.getSolutionId().solutionId
        val familyGroupInformation = familyVaultBackendProxy.getFamilyGroupName(GetFamilyGroupNameRequest(contextId))

        familyGroupSessionService.assignSession(
            AppConfig.PRIVMX_BRIDGE_URL, familyGroupInformation.familyGroupName, solutionId, contextId, keyPair
        )
        familyGroupSessionService.connect()
        familyGroupCredentialsRepository.addDefaultCredential(
            contextId,
            solutionId,
            contextId,
            keyPair, // TODO: Dodać tutaj jako name, nazwe grupy rodzinnej,
            encryptedPassword
        )
    }

    override suspend fun assignDefaultStoredFamilyGroup(): Boolean {
        val credential = familyGroupCredentialsRepository.getDefaultCredential()

        if (credential != null) {
            val familyGroupInformation = familyVaultBackendProxy.getFamilyGroupName(GetFamilyGroupNameRequest(credential.contextId))

            familyGroupSessionService.assignSession(
                AppConfig.PRIVMX_BRIDGE_URL,
                familyGroupInformation.familyGroupName,
                credential.solutionId,
                credential.contextId,
                PublicEncryptedPrivateKeyPair(credential.publicKey, credential.encryptedPrivateKey)
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

    override suspend fun refreshCurrentFamilyGroupName(
    ) {
        val contextId = familyGroupSessionService.getContextId()

        val familyGroupInformation = familyVaultBackendProxy.getFamilyGroupName(
            GetFamilyGroupNameRequest(
                contextId
            )
        )

        familyGroupCredentialsRepository.updateCredentialFamilyGroupName(contextId, familyGroupInformation.familyGroupName)
    }

    override suspend fun removeMemberFromFamilyGroup(
        contextId: String, userPubKey: String
    ) {
        familyVaultBackendProxy.removeMemberFromFamilyGroup(
            RemoveMemberFromFamilyGroupRequest(
                contextId, userPubKey
            )
        )
    }
}