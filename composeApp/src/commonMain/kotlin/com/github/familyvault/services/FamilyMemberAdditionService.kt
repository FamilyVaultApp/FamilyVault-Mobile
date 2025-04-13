package com.github.familyvault.services

import com.github.familyvault.backend.client.IFamilyVaultBackendClient
import com.github.familyvault.backend.requests.AddMemberToFamilyGroupRequest
import com.github.familyvault.models.NewFamilyMemberData

class FamilyMemberAdditionService(
    private val familyVaultBackendClient: IFamilyVaultBackendClient,
    private val sessionService: IFamilyGroupSessionService,
    private val chatService: IChatService
) : IFamilyMemberAdditionService {
    override suspend fun addMemberToFamilyGroup(
        contextId: String, newFamilyMember: NewFamilyMemberData
    ) {
        familyVaultBackendClient.addMemberToFamilyGroup(
            AddMemberToFamilyGroupRequest(
                contextId, newFamilyMember.fullname, newFamilyMember.keyPair.publicKey
            )
        )
    }

    override suspend fun afterJoinedToFamilyMembersOperations() {
        val currentUser = sessionService.getCurrentUser()

        chatService.createIndividualChatsWithAllFamilyMembersForMember(
            member = currentUser
        )
    }
}