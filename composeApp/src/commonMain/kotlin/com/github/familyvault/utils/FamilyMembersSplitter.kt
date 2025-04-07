package com.github.familyvault.utils

import com.github.familyvault.models.FamilyMember
import com.github.familyvault.models.SplitFamilyMembers

object FamilyMembersSplitter : IFamilyMembersSplitter {
    override fun split(familyGroupMembers: List<FamilyMember>): SplitFamilyMembers {
        val guardians: MutableList<FamilyMember> = mutableListOf()
        val members: MutableList<FamilyMember> = mutableListOf()

        for (member in familyGroupMembers) {
            guardians.add(member)
            members.add(member)
        }

        return SplitFamilyMembers(guardians, members)
    }
}