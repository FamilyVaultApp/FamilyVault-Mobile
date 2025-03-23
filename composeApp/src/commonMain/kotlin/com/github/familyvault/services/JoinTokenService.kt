package com.github.familyvault.services

import com.github.familyvault.backend.client.FamilyVaultBackendClient
import com.github.familyvault.backend.requests.GetTokenStatusRequest
import com.github.familyvault.backend.requests.UpdateTokenStatusRequest
import com.github.familyvault.models.ContextIdInfo
import com.github.familyvault.models.FamilyMemberJoinStatus
import com.github.familyvault.models.enums.JoinTokenStatus

class JoinTokenService(): IJoinTokenService {
    private val familyVaultBackendProxy = FamilyVaultBackendClient()

    override suspend fun generateJoinToken(): FamilyMemberJoinStatus {
        return familyVaultBackendProxy.generateJoinToken().joinStatus
    }

    override suspend fun getJoinStatus(token: String): FamilyMemberJoinStatus {
        return familyVaultBackendProxy.getTokenStatus(GetTokenStatusRequest(token = token)).joinStatus
    }

    override suspend fun changeJoinStatusStateToPending(token: String): FamilyMemberJoinStatus {
        return familyVaultBackendProxy.updateTokenStatus(UpdateTokenStatusRequest(token, JoinTokenStatus.Pending.value, null)).joinStatus
    }

    override suspend fun changeJoinStatusStateToAccept(token: String, contextId: String): FamilyMemberJoinStatus {
        return familyVaultBackendProxy.updateTokenStatus(UpdateTokenStatusRequest(token, JoinTokenStatus.Success.value, ContextIdInfo(contextId, null))).joinStatus
    }
}