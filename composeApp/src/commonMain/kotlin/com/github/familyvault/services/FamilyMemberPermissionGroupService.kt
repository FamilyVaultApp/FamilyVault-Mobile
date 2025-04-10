package com.github.familyvault.services

import com.github.familyvault.backend.client.FamilyVaultBackendClient
import com.github.familyvault.backend.requests.ChangeFamilyMemberPermissionGroupRequest
import com.github.familyvault.models.enums.FamilyGroupMemberPermissionGroup

class FamilyMemberPermissionGroupService(private val familyGroupSessionService: IFamilyGroupSessionService): IFamilyMemberPermissionGroupService {
    private val familyVaultBackendProxy = FamilyVaultBackendClient()

    override suspend fun changeFamilyMemberPermissionGroup(userId: String, permissionGroup: FamilyGroupMemberPermissionGroup) {
        familyVaultBackendProxy.changeFamilyMemberPermissionGroup(
            ChangeFamilyMemberPermissionGroupRequest(familyGroupSessionService.getContextId(), userId, permissionGroup)
        )
    }

}