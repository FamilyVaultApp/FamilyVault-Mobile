package com.github.familyvault.forms

import com.github.familyvault.forms.models.FormDataStringEntry
import com.github.familyvault.forms.validator.FormValidator
import com.github.familyvault.forms.validator.FormValidatorError

data class SelfHostingAddressFormData(
    val address: FormDataStringEntry = FormDataStringEntry()
)

class SelfHostingAddressForm : BaseForm() {
    private var formData: SelfHostingAddressFormData = SelfHostingAddressFormData()

    val address: String
        get() = formData.address.value

    val addressValidationError: FormValidatorError?
        get() = formData.address.validationError

    fun setAddress(address: String) {
        formData.address.value = address
        afterEntryUpdate(formData.address)
    }

    override fun validateForm() {
        formData.address.validationError = FormValidator.validateEmpty(address)
    }

    override fun isFormValid(): Boolean {
        return formData.address.isValid()
    }
}