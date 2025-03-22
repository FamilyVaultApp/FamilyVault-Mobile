package com.github.familyvault.services

import com.github.familyvault.models.FamilyMemberJoinStatus

interface IJoinTokenService {
    suspend fun generateJoinToken(): FamilyMemberJoinStatus

    suspend fun getJoinStatus(token: String): FamilyMemberJoinStatus

    suspend fun changeJoinStatusStateToAccept(token: String, contextId: String): FamilyMemberJoinStatus

    suspend fun changeJoinStatusStateToPending(token: String): FamilyMemberJoinStatus

}