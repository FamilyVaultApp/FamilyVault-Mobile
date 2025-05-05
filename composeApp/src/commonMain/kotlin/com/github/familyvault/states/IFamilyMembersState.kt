package com.github.familyvault.states

import com.github.familyvault.models.FamilyMember

interface IFamilyMembersState {
    suspend fun populateFamilyGroupMembersFromService()
    fun getAllFamilyMembers(): List<FamilyMember>
    fun getFamilyMemberByPubKey(pubKey: String): FamilyMember?
}