package com.github.familyvault.forms

import com.github.familyvault.forms.validator.FormValidator
import com.github.familyvault.forms.validator.FormValidatorError
import com.github.familyvault.models.forms.FormDataStringEntry
import com.github.familyvault.models.forms.FormDataStringTouchedEntry

data class TaskFormData(
    val title: FormDataStringEntry = FormDataStringEntry(),
    val description: FormDataStringTouchedEntry = FormDataStringTouchedEntry()
)

class TaskForm : BaseForm() {
    var formData: TaskFormData = TaskFormData()
        private set

    val title: String
        get() = formData.title.value

    val titleValidationError: FormValidatorError?
        get() = formData.title.getValidationErrorIfTouched()

    val description: String
        get() = formData.description.value

    val descriptionValidationError: FormValidatorError?
        get() = formData.description.getValidationErrorIfTouched()

    fun setTitle(title: String) {
        formData.title.value = title
        afterEntryUpdate(formData.title)
    }

    fun setDescription(description: String) {
        formData.description.value = description
        afterEntryUpdate(formData.description)
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
        return formData.title.isValid() && formData.description.isValid()
    }
}