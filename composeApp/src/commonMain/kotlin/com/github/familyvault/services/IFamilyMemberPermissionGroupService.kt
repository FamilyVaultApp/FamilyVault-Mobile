package com.github.familyvault.services

import com.github.familyvault.models.enums.FamilyGroupMemberPermissionGroup

interface IFamilyMemberPermissionGroupService {
    suspend fun changeFamilyMemberPermissionGroup(userId: String, permissionGroup: FamilyGroupMemberPermissionGroup)
}