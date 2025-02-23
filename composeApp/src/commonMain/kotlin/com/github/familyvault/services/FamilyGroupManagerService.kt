package com.github.familyvault.services

import com.github.familyvault.AppConfig
import com.github.familyvault.backend.client.FamilyVaultBackendClient
import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.backend.requests.AddMemberToFamilyRequest
import com.github.familyvault.backend.requests.CreateFamilyGroupRequest
import com.github.familyvault.models.FamilyGroupSession

class FamilyGroupManagerService(
    private val privMxClient: IPrivMxClient,
    private val familyGroupSessionService: FamilyGroupSessionService
) :
    IFamilyGroupManagerService {
    private val familyVaultBackendProxy = FamilyVaultBackendClient()

    override suspend fun createFamilyGroup(
        firstname: String,
        surname: String,
        secret: String,
        familyGroupName: String,
        familyGroupDescription: String?
    ) {
        val solutionId = familyVaultBackendProxy.getSolutionId().solutionId
        val pairOfKeys = privMxClient.generatePairOfPrivateAndPublicKey(secret, AppConfig.SALT)
        val username = "$firstname $surname"

        val createFamilyGroupResponse = familyVaultBackendProxy.createFamilyGroup(
            CreateFamilyGroupRequest(familyGroupName, familyGroupDescription ?: "Test description")
        )
        familyVaultBackendProxy.addGuardianToFamilyGroup(
            AddMemberToFamilyRequest(
                createFamilyGroupResponse.contextId,
                username,
                pairOfKeys.publicKey
            )
        )

        familyGroupSessionService.assignNewSession(
            FamilyGroupSession(
                AppConfig.PRIVMX_BRIDGE_URL,
                solutionId,
                createFamilyGroupResponse.contextId,
                pairOfKeys
            )
        )
        familyGroupSessionService.connect()
    }
}