package com.github.familyvault.utils

import com.github.familyvault.models.FamilyMember
import com.github.familyvault.models.SplitFamilyMembers
import com.github.familyvault.models.enums.FamilyGroupMemberPermissionGroup

object FamilyMembersSplitter : IFamilyMembersSplitter {
    override fun split(familyGroupMembers: List<FamilyMember>, currentMember: FamilyMember?): SplitFamilyMembers {
        val guardians: MutableList<FamilyMember> = mutableListOf()
        val members: MutableList<FamilyMember> = mutableListOf()
        if (currentMember != null) {
            if (currentMember.permissionGroup != FamilyGroupMemberPermissionGroup.Guardian) {
                guardians.add(currentMember)
            }
        }
        for (member in familyGroupMembers) {
            if (member.permissionGroup == FamilyGroupMemberPermissionGroup.Guardian) {
                guardians.add(member)
            }
            members.add(member)
        }

        return SplitFamilyMembers(guardians, members)
    }
}