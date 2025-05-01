package com.github.familyvault.utils

import com.github.familyvault.models.FamilyMember
import com.github.familyvault.models.SplitFamilyMembers

interface IFamilyMembersSplitter {
    fun split(familyGroupMembers: List<FamilyMember>): SplitFamilyMembers

    fun splitWithProvidedMembersAsManagers(familyGroupMembers: List<FamilyMember>, additionalManagers: List<FamilyMember?>): SplitFamilyMembers
}