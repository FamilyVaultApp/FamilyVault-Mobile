package com.github.familyvault.services

import com.github.familyvault.AppConfig
import com.github.familyvault.backend.client.IFamilyVaultBackendClient
import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.backend.exceptions.FamilyVaultBackendNoConnectionException
import com.github.familyvault.backend.requests.AddMemberToFamilyGroupRequest
import com.github.familyvault.backend.requests.CreateFamilyGroupRequest
import com.github.familyvault.backend.requests.GetFamilyGroupNameRequest
import com.github.familyvault.backend.requests.GetMemberFromFamilyGroupRequest
import com.github.familyvault.backend.requests.ListMembersFromFamilyGroupRequest
import com.github.familyvault.backend.requests.RemoveMemberFromFamilyGroupRequest
import com.github.familyvault.backend.requests.RenameFamilyGroupRequest
import com.github.familyvault.backend.responses.GetFamilyGroupNameResponse
import com.github.familyvault.models.FamilyMember
import com.github.familyvault.models.MemberIdentifier
import com.github.familyvault.models.PublicEncryptedPrivateKeyPair
import com.github.familyvault.models.enums.ConnectionStatus
import com.github.familyvault.models.enums.FamilyGroupMemberPermissionGroup
import com.github.familyvault.repositories.IFamilyGroupCredentialsRepository
import kotlinx.serialization.json.Json
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class FamilyGroupService(
    private val familyGroupCredentialsRepository: IFamilyGroupCredentialsRepository,
    private val familyGroupSessionService: IFamilyGroupSessionService,
    private val familyVaultBackendClient: IFamilyVaultBackendClient,
    private val privMxClient: IPrivMxClient,
) : IFamilyGroupService {

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun createFamilyGroupAndAssign(
        firstname: String,
        surname: String,
        password: String,
        familyGroupName: String,
        familyGroupDescription: String?
    ) {
        val solutionId = familyVaultBackendClient.getSolutionId().solutionId
        val pairOfKeys = privMxClient.generatePairOfPrivateAndPublicKey(password)
        val encryptedPassword = privMxClient.encryptPrivateKeyPassword(password)
        val memberIdentifier = MemberIdentifier(Uuid.random().toString(), firstname, surname)

        val contextId = familyVaultBackendClient.createFamilyGroup(
            CreateFamilyGroupRequest(familyGroupName, familyGroupDescription ?: "Test description")
        ).contextId
        familyVaultBackendClient.addMemberToFamilyGroup(
            AddMemberToFamilyGroupRequest(
                contextId,
                Json.encodeToString(memberIdentifier),
                pairOfKeys.publicKey,
                FamilyGroupMemberPermissionGroup.Guardian
            )
        )
        if (familyGroupSessionService.isSessionAssigned()) {
            familyGroupSessionService.disconnect()
        }
        familyGroupSessionService.assignSession(
            AppConfig.PRIVMX_BRIDGE_URL, familyGroupName, solutionId, contextId, pairOfKeys
        )
        familyGroupSessionService.connect()
        familyGroupCredentialsRepository.addDefaultCredential(
            familyGroupName, solutionId, contextId, pairOfKeys, encryptedPassword, firstname, surname
        )
    }

    override suspend fun joinFamilyGroupAndAssign(
        firstname: String,
        surname: String,
        encryptedPassword: String,
        keyPair: PublicEncryptedPrivateKeyPair,
        contextId: String
    ) {
        val solutionId = familyVaultBackendClient.getSolutionId().solutionId
        val familyGroupInformation =
            familyVaultBackendClient.getFamilyGroupName(GetFamilyGroupNameRequest(contextId))

        if (familyGroupSessionService.isSessionAssigned()) {
            familyGroupSessionService.disconnect()
        }

        familyGroupSessionService.assignSession(
            AppConfig.PRIVMX_BRIDGE_URL,
            familyGroupInformation.familyGroupName,
            solutionId,
            contextId,
            keyPair
        )
        familyGroupSessionService.connect()
        familyGroupCredentialsRepository.addDefaultCredential(
            familyGroupInformation.familyGroupName,
            solutionId,
            contextId,
            keyPair,
            encryptedPassword,
            firstname,
            surname
        )
    }

    override suspend fun assignDefaultStoredFamilyGroup(): ConnectionStatus {
        val credential = familyGroupCredentialsRepository.getDefaultCredential()
            ?: return ConnectionStatus.NoCredentials

        val familyGroupInformation: GetFamilyGroupNameResponse

        try {
            familyGroupInformation =
                familyVaultBackendClient.getFamilyGroupName(GetFamilyGroupNameRequest(credential.contextId))
        } catch (e: FamilyVaultBackendNoConnectionException) {
            return ConnectionStatus.Error
        }

        familyGroupSessionService.assignSession(
            AppConfig.PRIVMX_BRIDGE_URL,
            familyGroupInformation.familyGroupName,
            credential.solutionId,
            credential.contextId,
            PublicEncryptedPrivateKeyPair(credential.publicKey, credential.encryptedPrivateKey)
        )

        return familyGroupSessionService.connect()
    }

    override suspend fun addMemberToFamilyGroup(
        contextId: String, userId: String, userPubKey: String
    ) {
        familyVaultBackendClient.addMemberToFamilyGroup(
            AddMemberToFamilyGroupRequest(
                contextId, userId, userPubKey
            )
        )
    }

    override suspend fun retrieveFamilyGroupMembersList(): List<FamilyMember> {
        val contextId = familyGroupSessionService.getContextId()

        return familyVaultBackendClient.listMembersOfFamilyGroup(
            ListMembersFromFamilyGroupRequest(
                contextId
            )
        ).members
    }

    override suspend fun renameCurrentFamilyGroup(
        name: String
    ) {
        familyVaultBackendClient.renameFamilyGroup(
            RenameFamilyGroupRequest(
                familyGroupSessionService.getContextId(), name
            )
        )
    }

    override suspend fun refreshCurrentFamilyGroupName(
    ) {
        val contextId = familyGroupSessionService.getContextId()

        val familyGroupInformation = familyVaultBackendClient.getFamilyGroupName(
            GetFamilyGroupNameRequest(
                contextId
            )
        )

        familyGroupCredentialsRepository.updateCredentialFamilyGroupName(
            contextId, familyGroupInformation.familyGroupName
        )
    }

    override suspend fun retrieveFamilyGroupMembersWithoutMeList(): List<FamilyMember> {
        val contextId = familyGroupSessionService.getContextId()
        val familyMembers = familyVaultBackendClient.listMembersOfFamilyGroup(
            ListMembersFromFamilyGroupRequest(
                contextId
            )
        ).members.toMutableList()

        val myUserData = retrieveMyFamilyMemberData()
        return familyMembers.filter { it != myUserData }
    }

    override suspend fun retrieveMyFamilyMemberData(): FamilyMember {
        val contextId = familyGroupSessionService.getContextId()
        val publicKey = familyGroupSessionService.getPublicKey()

        return familyVaultBackendClient.getMemberFromFamilyGroup(
            GetMemberFromFamilyGroupRequest(
                contextId,
                null,
                publicKey
            )
        ).member
    }

    override suspend fun retrieveFamilyMemberDataByPublicKey(publicKey: String): FamilyMember {
        val contextId = familyGroupSessionService.getContextId()

        return familyVaultBackendClient.getMemberFromFamilyGroup(
            GetMemberFromFamilyGroupRequest(
                contextId,
                null,
                publicKey
            )
        ).member
    }

    override suspend fun removeMemberFromCurrentFamilyGroup(
        userPubKey: String
    ) {
        familyVaultBackendClient.removeMemberFromFamilyGroup(
            RemoveMemberFromFamilyGroupRequest(
                familyGroupSessionService.getContextId(), userPubKey
            )
        )
    }
}