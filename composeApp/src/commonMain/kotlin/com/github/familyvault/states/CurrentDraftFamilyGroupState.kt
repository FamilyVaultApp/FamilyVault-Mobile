package com.github.familyvault.states

import com.github.familyvault.forms.FamilyGroupNameFormData
import com.github.familyvault.forms.FamilyMemberFormData

class CurrentDraftFamilyGroupState : ICurrentDraftFamilyGroupState {
    private var familyGroupName: FamilyGroupNameFormData? = null
    private var familyMember: FamilyMemberFormData? = null

    override fun setDraftFamilyGroupName(value: FamilyGroupNameFormData) {
        familyGroupName = value
    }

    override fun setDraftFamilyMember(value: FamilyMemberFormData) {
        familyMember = value
    }

    override fun getNotNullDraftFamilyGroupName(): FamilyGroupNameFormData = requireNotNull(familyGroupName)

    override fun getNotNullDraftFamilyMember(): FamilyMemberFormData = requireNotNull(familyMember)
}