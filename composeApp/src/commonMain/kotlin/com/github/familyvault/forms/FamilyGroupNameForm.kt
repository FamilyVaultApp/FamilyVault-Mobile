package com.github.familyvault.forms

import com.github.familyvault.forms.validator.FormValidator
import com.github.familyvault.forms.validator.FormValidatorError
import com.github.familyvault.models.forms.FormDataStringEntry

data class FamilyGroupNameFormData(
    val familyGroupName: FormDataStringEntry = FormDataStringEntry(),
)

class FamilyGroupNameForm : BaseForm() {
    var formData = FamilyGroupNameFormData()
        private set

    val familyGroupName: String
        get() = formData.familyGroupName.value

    val familyGroupNameValidationError: FormValidatorError?
        get() = formData.familyGroupName.getValidationErrorIfTouched()

    fun setFamilyGroupName(familyGroupName: String) {
        formData.familyGroupName.value = familyGroupName
        afterEntryUpdate(formData.familyGroupName)
    }

    override fun validateForm() {
        formData.familyGroupName.validationError = FormValidator.multipleValidators(
            FormValidator.validateEmpty(familyGroupName),
            FormValidator.validateTooLong(familyGroupName),
        )
    }

    override fun isFormValid(): Boolean {
        return formData.familyGroupName.isValid()
    }

}