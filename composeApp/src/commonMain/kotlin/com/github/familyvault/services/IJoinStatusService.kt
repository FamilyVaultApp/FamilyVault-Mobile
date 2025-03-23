package com.github.familyvault.services

import com.github.familyvault.models.JoinStatus
import com.github.familyvault.models.JoinStatusInfo

interface IJoinStatusService {
    suspend fun generateJoinStatus(): JoinStatus
    suspend fun getJoinStatus(token: String): JoinStatus
    suspend fun waitAndGetJoinStatusInfo(token: String): JoinStatusInfo
    suspend fun waitForNotInitiatedStatus(token: String): JoinStatus
    suspend fun changeStateToPending(token: String): JoinStatus
    suspend fun changeStateToSuccess(
        token: String,
        contextId: String

    ): JoinStatus
}