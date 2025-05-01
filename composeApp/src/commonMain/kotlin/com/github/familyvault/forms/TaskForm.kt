package com.github.familyvault.forms

import com.github.familyvault.forms.models.FormDataNullableStringTouchedEntry
import com.github.familyvault.forms.models.FormDataStringEntry
import com.github.familyvault.forms.models.FormDataStringTouchedEntry
import com.github.familyvault.forms.validator.FormValidator
import com.github.familyvault.forms.validator.FormValidatorError

data class TaskFormData(
    val title: FormDataStringEntry = FormDataStringEntry(),
    val description: FormDataStringTouchedEntry = FormDataStringTouchedEntry(),
    val pubKeyOfAssignedMember: FormDataNullableStringTouchedEntry = FormDataNullableStringTouchedEntry()
)

class TaskForm : BaseForm() {
    private var formData: TaskFormData = TaskFormData()

    val title: String
        get() = formData.title.value

    val titleValidationError: FormValidatorError?
        get() = formData.title.getValidationErrorIfTouched()

    val description: String
        get() = formData.description.value

    val descriptionValidationError: FormValidatorError?
        get() = formData.description.getValidationErrorIfTouched()

    val pubKeyOfAssignedMember: String?
        get() = formData.pubKeyOfAssignedMember.value

    val pubKeyOfAssignedMemberValidationError: FormValidatorError?
        get() = formData.pubKeyOfAssignedMember.getValidationErrorIfTouched()

    fun setTitle(title: String) {
        formData.title.value = title
        afterEntryUpdate(formData.title)
    }

    fun setDescription(description: String) {
        formData.description.value = description
        afterEntryUpdate(formData.description)
    }

    fun setPubKeyOfAssignedMember(pubKey: String?) {
        formData.pubKeyOfAssignedMember.value = pubKey
        afterEntryUpdate(formData.pubKeyOfAssignedMember)
    }

    override fun validateForm() {
        formData.title.validationError = FormValidator.multipleValidators(
            FormValidator.validateEmpty(title), FormValidator.validateTooLong(title, maxLength = 32)
        )

        formData.description.validationError = FormValidator.multipleValidators(
            FormValidator.validateTooLong(description, maxLength = 255)
        )
    }

    override fun isFormValid(): Boolean {
        return formData.title.isValid() && formData.description.isValid() && formData.pubKeyOfAssignedMember.isValid()
    }
}