package com.github.familyvault.forms

import com.github.familyvault.forms.models.FormDataStringEntry
import com.github.familyvault.forms.validator.FormValidator
import com.github.familyvault.forms.validator.FormValidatorError

data class TasksListFormData(
    val name: FormDataStringEntry = FormDataStringEntry()
)

class TaskListForm : BaseForm() {
    private var formData: TasksListFormData = TasksListFormData()

    val name: String
        get() = formData.name.value

    val taskListNameValidationError: FormValidatorError?
        get() = formData.name.getValidationErrorIfTouched()

    fun setName(name: String) {
        formData.name.value = name
        afterEntryUpdate(formData.name)
    }

    fun setNameWithoutTouching(name: String) {
        formData.name.value = name
    }

    override fun validateForm() {
        formData.name.validationError = FormValidator.multipleValidators(
            FormValidator.validateEmpty(name),
            FormValidator.validateTooLong(name, maxLength = 32)
        )
    }

    override fun isFormValid(): Boolean {
        return formData.name.isValid()
    }
}