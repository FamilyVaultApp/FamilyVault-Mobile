package com.github.familyvault.services

import com.github.familyvault.AppConfig
import com.github.familyvault.backend.client.FamilyVaultBackendClient
import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.backend.requests.AddMemberToFamilyGroupRequest
import com.github.familyvault.backend.requests.CreateFamilyGroupRequest
import com.github.familyvault.backend.requests.ListMembersFromFamilyGroupRequest
import com.github.familyvault.models.FamilyMember
import com.github.familyvault.models.PublicPrivateKeyPair
import com.github.familyvault.repositories.IFamilyGroupCredentialsRepository

class FamilyGroupService(
    private val privMxClient: IPrivMxClient,
    private val familyGroupSessionService: IFamilyGroupSessionService,
    private val familyGroupCredentialsRepository: IFamilyGroupCredentialsRepository
) :
    IFamilyGroupService {
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
        familyVaultBackendProxy.addGuardianToFamilyGroup(
            AddMemberToFamilyGroupRequest(
                contextId,
                username,
                pairOfKeys.publicKey
            )
        )
        familyGroupSessionService.assignSession(
            AppConfig.PRIVMX_BRIDGE_URL,
            solutionId,
            contextId,
            pairOfKeys
        )
        familyGroupSessionService.connect()
        familyGroupCredentialsRepository.addDefaultCredential(
            familyGroupName,
            solutionId,
            contextId,
            pairOfKeys
        )
    }

    override suspend fun assignDefaultStoredFamilyGroup(): Boolean {
        val credential = familyGroupCredentialsRepository.getDefaultCredential()

        if (credential != null) {
            familyGroupSessionService.assignSession(
                AppConfig.PRIVMX_BRIDGE_URL,
                credential.solutionId,
                credential.contextId,
                PublicPrivateKeyPair(credential.publicKey, credential.privateKey)
            )
            familyGroupSessionService.connect()
            return true
        }
        return false
    }

    override suspend fun retrieveFamilyGroupMembersList(): List<FamilyMember> {
        val contextId = familyGroupSessionService.getContextId()

        return familyVaultBackendProxy.listMembersOfFamilyGroup(ListMembersFromFamilyGroupRequest(contextId)).members
    }
}