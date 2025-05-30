package com.github.familyvault.states

import com.github.familyvault.forms.FamilyGroupNameFormData
import com.github.familyvault.forms.FamilyMemberFormData

interface ICurrentDraftFamilyGroupState {
    fun setDraftFamilyGroupName(value: FamilyGroupNameFormData)
    fun setDraftFamilyMember(value: FamilyMemberFormData)
    fun getNotNullDraftFamilyGroupName(): FamilyGroupNameFormData
    fun getNotNullDraftFamilyMember(): FamilyMemberFormData
}