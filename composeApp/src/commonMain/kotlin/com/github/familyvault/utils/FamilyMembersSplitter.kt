package com.github.familyvault.utils

import com.github.familyvault.models.FamilyMember
import com.github.familyvault.models.SplitFamilyMembers
import com.github.familyvault.models.enums.FamilyGroupMemberPermissionGroup

object FamilyMembersSplitter : IFamilyMembersSplitter {
    override fun split(familyGroupMembers: List<FamilyMember>): SplitFamilyMembers {
        val guardians: MutableList<FamilyMember> = mutableListOf()
        val members: MutableList<FamilyMember> = mutableListOf()

        for (member in familyGroupMembers) {
            if (member.permissionGroup == FamilyGroupMemberPermissionGroup.Guardian) {
                guardians.add(member)
            }
            members.add(member)
        }

        return SplitFamilyMembers(guardians.distinct(), members.distinct())
    }

    override fun splitWithProvidedMembersAsManagers(
        familyGroupMembers: List<FamilyMember>,
        additionalManagers: List<FamilyMember?>
    ): SplitFamilyMembers {
        val guardians: MutableList<FamilyMember> = mutableListOf()
        val members: MutableList<FamilyMember> = mutableListOf()

        additionalManagers.filterNotNull().forEach { manager ->
            guardians.add(manager)
        }

        for (member in familyGroupMembers) {
            if (member.permissionGroup == FamilyGroupMemberPermissionGroup.Guardian) {
                guardians.add(member)
            }
            members.add(member)
        }
        return SplitFamilyMembers(guardians.distinct(), members.distinct())
    }
}