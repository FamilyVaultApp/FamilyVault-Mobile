package com.github.familyvault.backend.client

import com.github.familyvault.backend.requests.AddMemberToFamilyGroupRequest
import com.github.familyvault.backend.requests.ChangeFamilyMemberPermissionGroupRequest
import com.github.familyvault.backend.requests.CreateFamilyGroupRequest
import com.github.familyvault.backend.requests.DeleteJoinStatusRequest
import com.github.familyvault.backend.requests.GetFamilyGroupNameRequest
import com.github.familyvault.backend.requests.GetJoinStatusRequest
import com.github.familyvault.backend.requests.ListMembersFromFamilyGroupRequest
import com.github.familyvault.backend.requests.RemoveMemberFromFamilyGroupRequest
import com.github.familyvault.backend.requests.RenameFamilyGroupRequest
import com.github.familyvault.backend.requests.UpdateJoinStatusRequest
import com.github.familyvault.backend.responses.AddMemberToFamilyGroupResponse
import com.github.familyvault.backend.responses.ChangeFamilyMemberPermissionGroup
import com.github.familyvault.backend.responses.CreateFamilyGroupResponse
import com.github.familyvault.backend.responses.DeleteJoinStatusResponse
import com.github.familyvault.backend.responses.GenerateJoinStatusResponse
import com.github.familyvault.backend.responses.GetFamilyGroupNameResponse
import com.github.familyvault.backend.responses.GetJoinStatusResponse
import com.github.familyvault.backend.responses.ListMembersFromFamilyGroupResponse
import com.github.familyvault.backend.responses.PrivMxSolutionIdResponse
import com.github.familyvault.backend.responses.RemoveMemberFromFamilyGroupResponse
import com.github.familyvault.backend.responses.RenameFamilyGroupResponse
import com.github.familyvault.backend.responses.UpdateJoinStatusResponse

interface IFamilyVaultBackendClient {
    suspend fun getSolutionId(): PrivMxSolutionIdResponse
    suspend fun createFamilyGroup(req: CreateFamilyGroupRequest): CreateFamilyGroupResponse
    suspend fun addMemberToFamilyGroup(req: AddMemberToFamilyGroupRequest): AddMemberToFamilyGroupResponse
    suspend fun changeFamilyMemberPermissionGroup(req: ChangeFamilyMemberPermissionGroupRequest): ChangeFamilyMemberPermissionGroup
    suspend fun listMembersOfFamilyGroup(req: ListMembersFromFamilyGroupRequest): ListMembersFromFamilyGroupResponse
    suspend fun renameFamilyGroup(req: RenameFamilyGroupRequest): RenameFamilyGroupResponse
    suspend fun getFamilyGroupName(req: GetFamilyGroupNameRequest): GetFamilyGroupNameResponse
    suspend fun generateJoinStatus(): GenerateJoinStatusResponse
    suspend fun getJoinStatus(req: GetJoinStatusRequest): GetJoinStatusResponse
    suspend fun updateJoinStatus(req: UpdateJoinStatusRequest): UpdateJoinStatusResponse
    suspend fun deleteJoinStatus(req: DeleteJoinStatusRequest): DeleteJoinStatusResponse
    suspend fun removeMemberFromFamilyGroup(req: RemoveMemberFromFamilyGroupRequest) : RemoveMemberFromFamilyGroupResponse
}