package com.github.familyvault.backend.client

import com.github.familyvault.backend.requests.AddMemberToFamilyGroupRequest
import com.github.familyvault.backend.requests.CreateFamilyGroupRequest
import com.github.familyvault.backend.requests.ListMembersFromFamilyGroupRequest
import com.github.familyvault.backend.responses.CreateFamilyGroupResponse
import com.github.familyvault.backend.responses.ListMembersFromFamilyGroupResponse
import com.github.familyvault.backend.responses.PrivMxSolutionIdResponse

interface IFamilyVaultBackendClient {
    suspend fun getSolutionId(): PrivMxSolutionIdResponse
    suspend fun createFamilyGroup(req: CreateFamilyGroupRequest) : CreateFamilyGroupResponse
    suspend fun addGuardianToFamilyGroup(req: AddMemberToFamilyGroupRequest)
    suspend fun addMemberToFamilyGroup(req: AddMemberToFamilyGroupRequest)
    suspend fun addGuestToFamilyGroup(req: AddMemberToFamilyGroupRequest)
    suspend fun listMembersOfFamilyGroup(req: ListMembersFromFamilyGroupRequest): ListMembersFromFamilyGroupResponse
}