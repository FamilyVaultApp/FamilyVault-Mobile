package com.github.familyvault.models.enums

import com.github.familyvault.utils.EnumSerializer
import kotlinx.serialization.Serializable

private class FamilyGroupMemberPermissionGroupSerializer: EnumSerializer<FamilyGroupMemberPermissionGroup>(
    "FamilyGroupMemberPermissionGroup",
    { it.value },
    { v -> FamilyGroupMemberPermissionGroup.entries.first { it.value == v } }
)

@Serializable(with = FamilyGroupMemberPermissionGroupSerializer::class)
enum class FamilyGroupMemberPermissionGroup(val value: Int) {
    Guardian(0),
    Member(1),
    Guest(2);
}