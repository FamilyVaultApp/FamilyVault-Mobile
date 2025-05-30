package com.github.familyvault.states

import com.github.familyvault.forms.FamilyMemberFormData

interface ICurrentDraftFamilyMemberState {
    fun setFamilyMember(value: FamilyMemberFormData)
    fun getNotNullFamilyMember(): FamilyMemberFormData
}