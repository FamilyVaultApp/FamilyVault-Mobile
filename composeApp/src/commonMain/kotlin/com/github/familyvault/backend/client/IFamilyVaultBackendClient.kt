package com.github.familyvault.backend.client

import com.github.familyvault.backend.requests.AddMemberToFamilyGroupRequest
import com.github.familyvault.backend.requests.CreateFamilyGroupRequest
import com.github.familyvault.backend.requests.GetTokenStatusRequest
import com.github.familyvault.backend.requests.ListMembersFromFamilyGroupRequest
import com.github.familyvault.backend.requests.UpdateTokenStatusRequest
import com.github.familyvault.backend.responses.CreateFamilyGroupResponse
import com.github.familyvault.backend.responses.GenerateJoinTokenResponse
import com.github.familyvault.backend.responses.GetTokenStatusResponse
import com.github.familyvault.backend.responses.ListMembersFromFamilyGroupResponse
import com.github.familyvault.backend.responses.PrivMxSolutionIdResponse
import com.github.familyvault.backend.responses.UpdateTokenStatusResponse

interface IFamilyVaultBackendClient {
    suspend fun getSolutionId(): PrivMxSolutionIdResponse
    suspend fun createFamilyGroup(req: CreateFamilyGroupRequest) : CreateFamilyGroupResponse
    suspend fun addMemberToFamilyGroup(req: AddMemberToFamilyGroupRequest)
    suspend fun listMembersOfFamilyGroup(req: ListMembersFromFamilyGroupRequest): ListMembersFromFamilyGroupResponse
    suspend fun generateJoinToken(): GenerateJoinTokenResponse
    suspend fun getTokenStatus(req: GetTokenStatusRequest): GetTokenStatusResponse
    suspend fun updateTokenStatus(req: UpdateTokenStatusRequest): UpdateTokenStatusResponse
}