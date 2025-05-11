package com.github.familyvault.forms

import com.github.familyvault.forms.models.FormDataStringEntry
import com.github.familyvault.forms.validator.FormValidator
import com.github.familyvault.forms.validator.FormValidatorError

data class BackendConfigurationFormData(
    val backendUrl: FormDataStringEntry = FormDataStringEntry(),
)

class BackendConfigurationForm : BaseForm() {
    var formData = BackendConfigurationFormData()
        private set

    val backendUrl: String
        get() = formData.backendUrl.value

    val backendUrlValidationError: FormValidatorError?
        get() = formData.backendUrl.getValidationErrorIfTouched()

    fun setBackendUrl(backendUrl: String) {
        formData.backendUrl.value = backendUrl
        afterEntryUpdate(formData.backendUrl)
    }

    override fun validateForm() {
        formData.backendUrl.validationError = FormValidator.validateEmpty(backendUrl)
    }

    override fun isFormValid(): Boolean {
        return formData.backendUrl.isValid()
    }

}