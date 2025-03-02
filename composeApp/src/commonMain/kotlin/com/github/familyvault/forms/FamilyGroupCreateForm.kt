package com.github.familyvault.forms

import com.github.familyvault.models.forms.FormDataStringEntry
import com.github.familyvault.forms.validator.FormValidator
import com.github.familyvault.forms.validator.FormValidatorError

data class FamilyGroupCreateFormData(
    val firstname: FormDataStringEntry = FormDataStringEntry(),
    val surname: FormDataStringEntry = FormDataStringEntry(),
    val familyGroupName: FormDataStringEntry = FormDataStringEntry(),
)

class FamilyGroupCreateForm : BasicForm() {
    var formData = FamilyGroupCreateFormData()
        private set

    val firstname: String
        get() = formData.firstname.value

    val surname: String
        get() = formData.surname.value

    val familyGroupName: String
        get() = formData.familyGroupName.value

    val firstnameValidationError: FormValidatorError?
        get() = formData.firstname.getValidationErrorIfTouched()

    val surnameValidationError: FormValidatorError?
        get() = formData.surname.getValidationErrorIfTouched()

    val familyGroupNameValidationError: FormValidatorError?
        get() = formData.familyGroupName.getValidationErrorIfTouched()


    fun setFirstname(firstname: String) {
        formData.firstname.value = firstname
        afterEntryUpdate(formData.firstname)
    }

    fun setSurname(surname: String) {
        formData.surname.value = surname
        afterEntryUpdate(formData.surname)
    }

    fun setFamilyGroupName(familyGroupName: String) {
        formData.familyGroupName.value = familyGroupName
        afterEntryUpdate(formData.familyGroupName)
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

        formData.familyGroupName.validationError = FormValidator.multipleValidators(
            FormValidator.validateEmpty(familyGroupName),
            FormValidator.validateTooLong(familyGroupName),
        )
    }

    override fun isFormValid(): Boolean {
        return formData.firstname.isValid() &&
                formData.surname.isValid() &&
                formData.familyGroupName.isValid()
    }
}



