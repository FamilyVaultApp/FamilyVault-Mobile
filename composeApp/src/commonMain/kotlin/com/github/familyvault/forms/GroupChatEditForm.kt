package com.github.familyvault.forms

import com.github.familyvault.forms.validator.FormValidator
import com.github.familyvault.forms.validator.FormValidatorError
import com.github.familyvault.models.FamilyMember
import com.github.familyvault.models.forms.FormDataListEntry
import com.github.familyvault.models.forms.FormDataStringEntry

data class GroupChatEditFormData(
    val groupName: FormDataStringEntry = FormDataStringEntry(),
    var familyMembers: FormDataListEntry<FamilyMember> = FormDataListEntry(),
)

class GroupChatEditForm : BaseForm() {

    var formData: GroupChatEditFormData = GroupChatEditFormData()
        private set

    val groupName: String
        get() = formData.groupName.value

    val familyMembers: List<FamilyMember>
        get() = formData.familyMembers.list

    var currentUserPublicKey: String = ""

    val groupNameValidationError: FormValidatorError?
        get() = formData.groupName.getValidationErrorIfTouched()

    val groupMembersValidationError: FormValidatorError?
        get() = formData.familyMembers.getValidationErrorIfTouched()

    fun setGroupName(groupName: String) {
        formData.groupName.value = groupName
        afterEntryUpdate(formData.groupName)
    }

    fun addMemberToGroupChat(member: FamilyMember) {
        formData.familyMembers.list.add(member)
        afterEntryUpdate(formData.familyMembers)
    }

    fun removeMemberFromGroupChat(member: FamilyMember) {
        formData.familyMembers.list.remove(member)
        afterEntryUpdate(formData.familyMembers)
    }

    fun addAllMembersFromListToGroupChat(members: List<FamilyMember>) {
        formData.familyMembers.list.addAll(members)
        afterEntryUpdate(formData.familyMembers)
    }

    override fun validateForm() {
        formData.groupName.validationError = FormValidator.multipleValidators(
            FormValidator.validateEmpty(groupName),
            FormValidator.validateTooLong(groupName),
        )

        formData.familyMembers.validationError = FormValidator.multipleValidators(
            FormValidator.validateGroupNotEmpty(familyMembers),
            FormValidator.validateCreatorIsGroupMember(familyMembers, currentUserPublicKey)
        )
    }

    override fun isFormValid(): Boolean {
        return formData.groupName.isValid() && formData.familyMembers.isValid()
    }

}