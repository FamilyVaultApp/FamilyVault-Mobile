package com.github.familyvault.forms

import com.github.familyvault.forms.validator.FormValidator
import com.github.familyvault.forms.validator.FormValidatorError
import com.github.familyvault.models.forms.FormDataStringEntry
import com.github.familyvault.models.forms.FormDataStringTouchedEntry

data class TaskFormData(
    val name: FormDataStringEntry = FormDataStringEntry(),
    val description: FormDataStringTouchedEntry = FormDataStringTouchedEntry()
)

class TaskForm : BaseForm() {
    var formData: TaskFormData = TaskFormData()
        private set

    val name: String
        get() = formData.name.value

    val nameValidationError: FormValidatorError?
        get() = formData.name.getValidationErrorIfTouched()

    val description: String
        get() = formData.description.value

    val descriptionValidationError: FormValidatorError?
        get() = formData.description.getValidationErrorIfTouched()

    fun setName(name: String) {
        formData.name.value = name
        afterEntryUpdate(formData.name)
    }

    fun setDescription(description: String) {
        formData.description.value = description
        afterEntryUpdate(formData.description)
    }

    override fun validateForm() {
        formData.name.validationError = FormValidator.multipleValidators(
            FormValidator.validateEmpty(name),
            FormValidator.validateTooLong(name, maxLength = 32)
        )

        formData.description.validationError = FormValidator.multipleValidators(
            FormValidator.validateTooLong(description, maxLength = 255)
        )
    }

    override fun isFormValid(): Boolean {
        return formData.description.isValid() && formData.name.isValid()
    }
}