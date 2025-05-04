package com.github.familyvault.forms

import com.github.familyvault.forms.models.FormDataStringEntry
import com.github.familyvault.forms.validator.FormValidator
import com.github.familyvault.forms.validator.FormValidatorError

data class FamilyMemberFormData(
    val firstname: FormDataStringEntry = FormDataStringEntry(),
    val surname: FormDataStringEntry = FormDataStringEntry(),
)

class FamilyGroupMemberForm : BaseForm() {
    var formData = FamilyMemberFormData()
        private set

    val firstname: String
        get() = formData.firstname.value

    val surname: String
        get() = formData.surname.value

    val firstnameValidationError: FormValidatorError?
        get() = formData.firstname.getValidationErrorIfTouched()

    val surnameValidationError: FormValidatorError?
        get() = formData.surname.getValidationErrorIfTouched()

    fun setFirstname(firstname: String) {
        formData.firstname.value = firstname
        afterEntryUpdate(formData.firstname)
    }

    fun setSurname(surname: String) {
        formData.surname.value = surname
        afterEntryUpdate(formData.surname)
    }

    override fun validateForm() {
        formData.firstname.validationError = FormValidator.multipleValidators(
            FormValidator.validateEmpty(firstname),
            FormValidator.validateTooLong(firstname),
        )

        formData.surname.validationError = FormValidator.multipleValidators(
            FormValidator.validateEmpty(surname),
            FormValidator.validateTooLong(surname),
        )
    }

    override fun isFormValid(): Boolean {
        return formData.firstname.isValid() &&
                formData.surname.isValid()
    }
}



