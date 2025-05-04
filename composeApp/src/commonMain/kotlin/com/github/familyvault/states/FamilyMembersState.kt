package com.github.familyvault.states

import com.github.familyvault.models.FamilyMember
import com.github.familyvault.services.FamilyGroupService

class FamilyMembersState(
    private val familyGroupService: FamilyGroupService
) : IFamilyMembersState {
    private val familyMembers: MutableList<FamilyMember> = mutableListOf()

    override suspend fun populateFamilyGroupMembersFromService() {
        familyMembers.clear()
        familyMembers.addAll(familyGroupService.retrieveFamilyGroupMembersList())
    }

    override fun getAllFamilyMembers(): List<FamilyMember> {
        return familyMembers
    }

    override fun getFamilyMemberByPubKey(pubKey: String): FamilyMember? {
        return familyMembers.firstOrNull { it.publicKey == pubKey }
    }
}