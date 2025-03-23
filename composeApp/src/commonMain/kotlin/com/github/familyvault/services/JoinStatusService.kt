package com.github.familyvault.services

import com.github.familyvault.AppConfig
import com.github.familyvault.backend.client.FamilyVaultBackendClient
import com.github.familyvault.backend.requests.GetJoinStatusRequest
import com.github.familyvault.backend.requests.UpdateJoinStatusRequest
import com.github.familyvault.models.JoinStatusInfo
import com.github.familyvault.models.JoinStatus
import com.github.familyvault.models.enums.JoinStatusState
import kotlinx.coroutines.delay

class JoinStatusService : IJoinStatusService {
    private val familyVaultBackendProxy = FamilyVaultBackendClient()

    override suspend fun generateJoinStatus(): JoinStatus {
        return familyVaultBackendProxy.generateJoinStatus().joinStatus
    }

    override suspend fun getJoinStatus(token: String): JoinStatus {
        return familyVaultBackendProxy.getJoinStatus(GetJoinStatusRequest(token = token)).joinStatus
    }

    override suspend fun waitAndGetJoinStatusInfo(token: String): JoinStatusInfo {
        var joinStatus: JoinStatus?

        do {
            delay(AppConfig.BACKEND_REQUEST_INTERVAL_LENGTH_MS)
            joinStatus = getJoinStatus(token)
        } while (joinStatus?.state == JoinStatusState.Pending)

        if (joinStatus?.info == null) {
            throw Exception("joinStatus or joinStatusInfo is null")
        }
        return joinStatus.info!!
    }

    override suspend fun waitForNotInitiatedStatus(token: String): JoinStatus {
        var joinStatus: JoinStatus?

        do {
            delay(AppConfig.BACKEND_REQUEST_INTERVAL_LENGTH_MS)
            joinStatus = getJoinStatus(token)
            println(joinStatus)
        } while (joinStatus?.state == JoinStatusState.Initiated)

        return joinStatus!!
    }

    override suspend fun changeStateToPending(token: String): JoinStatus {
        return changeJoinStatusState(
            token,
            JoinStatusState.Success,
            null
        )
    }

    override suspend fun changeStateToSuccess(
        token: String, contextId: String
    ): JoinStatus {
        return changeJoinStatusState(
            token,
            JoinStatusState.Success,
            JoinStatusInfo(contextId, null)
        )
    }

    private suspend fun changeJoinStatusState(
        token: String, joinTokenStatus: JoinStatusState, joinStatusInfo: JoinStatusInfo?
    ): JoinStatus {
        return familyVaultBackendProxy.updateJoinStatus(
            UpdateJoinStatusRequest(
                token, joinTokenStatus, joinStatusInfo
            )
        ).joinStatus
    }
}