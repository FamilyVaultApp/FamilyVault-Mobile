package com.github.familyvault.states

import com.github.familyvault.forms.FamilyMemberFormData

class CurrentDraftFamilyMemberState : ICurrentDraftFamilyMemberState {
    private var familyMemberDraft: FamilyMemberFormData? = null

    override fun setFamilyMember(value: FamilyMemberFormData) {
        familyMemberDraft = value
    }

    override fun getNotNullFamilyMember(): FamilyMemberFormData {
        return requireNotNull(familyMemberDraft)
    }
}