package com.github.familyvault.forms

import com.github.familyvault.models.forms.FormDataStringEntry
import com.github.familyvault.forms.validator.FormValidator
import com.github.familyvault.forms.validator.FormValidatorError

data class PrivateKeyAssignPasswordFormData(
    val password: FormDataStringEntry = FormDataStringEntry(),
    val repeatPassword: FormDataStringEntry = FormDataStringEntry()
)

class PrivateKeyAssignPasswordForm : BasicForm() {
    var formData: PrivateKeyAssignPasswordFormData = PrivateKeyAssignPasswordFormData()
        private set

    val password: String
        get() = formData.password.value

    val repeatPassword: String
        get() = formData.repeatPassword.value

    val passwordValidationError: FormValidatorError?
        get() = formData.password.getValidationErrorIfTouched()

    val passwordRepeatValidationError: FormValidatorError?
        get() = formData.repeatPassword.getValidationErrorIfTouched()

    fun setPassword(password: String) {
        formData.password.value = password
        afterEntryUpdate(formData.password)
    }

    fun setRepeatPassword(repeatPassword: String) {
        formData.repeatPassword.value = repeatPassword
        afterEntryUpdate(formData.repeatPassword)
    }

    override fun validateForm() {
        formData.password.validationError = FormValidator.multipleValidators(
            FormValidator.validateTooShort(password),
            FormValidator.validateEmpty(password)
        )

        formData.repeatPassword.validationError = FormValidator.multipleValidators(
            FormValidator.validateNotEqual(password, repeatPassword)
        )
    }

    override fun isFormValid(): Boolean {
        return formData.password.isValid() &&
                formData.repeatPassword.isValid()
    }
}