package com.github.familyvault.services

import com.github.familyvault.AppConfig
import com.github.familyvault.backend.client.FamilyVaultBackendClient
import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.backend.requests.AddMemberToFamilyRequest
import com.github.familyvault.backend.requests.CreateFamilyGroupRequest

class FamilyGroupManagerService(
    private val privMxClient: IPrivMxClient,
    private val currentSessionContextStore: CurrentSessionContextStore
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
        val pairOfKeys = privMxClient.generatePairOfPrivateAndPublicKey(secret, AppConfig.SALT)
        val username = "$firstname $surname"

        val createFamilyGroupResponse = familyVaultBackendProxy.createFamilyGroup(
            CreateFamilyGroupRequest(familyGroupName, familyGroupDescription ?: "Test description")
        )
        familyVaultBackendProxy.addGuardianToFamilyGroup(
            AddMemberToFamilyRequest(createFamilyGroupResponse.contextId, username, pairOfKeys.publicKey)
        )

        currentSessionContextStore.setPairOfKeys(pairOfKeys)
        currentSessionContextStore.setCurrentFamilyGroupId(createFamilyGroupResponse.contextId)
    }
}