package com.github.familyvault.backend.client

import com.github.familyvault.backend.requests.AddMemberToFamilyRequest
import com.github.familyvault.backend.requests.CreateFamilyGroupRequest
import com.github.familyvault.backend.responses.CreateFamilyGroupResponse
import com.github.familyvault.backend.responses.PrivMxSolutionIdResponse

interface IFamilyVaultBackendClient {
    suspend fun getSolutionId(): PrivMxSolutionIdResponse
    suspend fun createFamilyGroup(req: CreateFamilyGroupRequest) : CreateFamilyGroupResponse
    suspend fun addGuardianToFamilyGroup(req: AddMemberToFamilyRequest)
    suspend fun addMemberToFamilyGroup(req: AddMemberToFamilyRequest)
    suspend fun addGuestToFamilyGroup(req: AddMemberToFamilyRequest)
}