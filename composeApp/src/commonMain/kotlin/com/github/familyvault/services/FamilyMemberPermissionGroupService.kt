package com.github.familyvault.services

import com.github.familyvault.backend.client.IFamilyVaultBackendClient
import com.github.familyvault.backend.requests.ChangeFamilyMemberPermissionGroupRequest
import com.github.familyvault.models.enums.FamilyGroupMemberPermissionGroup

class FamilyMemberPermissionGroupService(
    private val familyGroupSessionService: IFamilyGroupSessionService,
    private val familyVaultBackendClient: IFamilyVaultBackendClient
) :
    IFamilyMemberPermissionGroupService {

    override suspend fun changeFamilyMemberPermissionGroup(
        userId: String,
        permissionGroup: FamilyGroupMemberPermissionGroup
    ) {
        familyVaultBackendClient.changeFamilyMemberPermissionGroup(
            ChangeFamilyMemberPermissionGroupRequest(
                familyGroupSessionService.getContextId(),
                userId,
                permissionGroup
            )
        )
    }

}