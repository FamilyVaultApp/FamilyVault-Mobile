package com.github.familyvault.forms

import com.github.familyvault.forms.validator.FormValidator
import com.github.familyvault.forms.validator.FormValidatorError
import com.github.familyvault.models.forms.FormDataStringEntry

data class TasksListFormData(
    val listName: FormDataStringEntry = FormDataStringEntry()
)

class TasksListForm : BasicForm() {
    var formData: TasksListFormData = TasksListFormData()
        private set

    val listName: String
        get() = formData.listName.value

    val taskListNameValidationError: FormValidatorError?
        get() = formData.listName.getValidationErrorIfTouched()

    fun setTaskListName(name: String) {
        formData.listName.value = name
        afterEntryUpdate(formData.listName)
    }

    override fun validateForm() {
        formData.listName.validationError = FormValidator.multipleValidators(
            FormValidator.validateEmpty(listName),
            FormValidator.validateTooLong(listName, maxLength = 32)
        )
    }

    override fun isFormValid(): Boolean {
        return formData.listName.isValid()
    }
}