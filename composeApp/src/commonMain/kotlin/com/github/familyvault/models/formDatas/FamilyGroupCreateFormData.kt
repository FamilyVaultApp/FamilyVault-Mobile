package com.github.familyvault.models.formDatas

import androidx.compose.runtime.Composable
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

data class FamilyGroupFormData(
    var firstname: String = "",
    var lastname: String = "",
    var familyGroupName: String = "",

    var firstNameError: String? = "",
    var lastNameError: String? = "",
    var familyGroupNameError: String? = "",
    var formIsCorrect: Boolean = false,
) {
    val fullname: String
        get() = "$firstname $lastname"

    private val validator = FormValidator()

    fun validateForm(): FamilyGroupFormData {
        val firstNameError = validator.validateEntry(firstname)
        val lastNameError = validator.validateEntry(lastname)
        val familyGroupNameError = validator.validateEntry(familyGroupName)
        if (firstNameError.isNullOrBlank() && lastNameError.isNullOrBlank() && familyGroupNameError.isNullOrBlank())
        {
            formIsCorrect = true
        } else {
            formIsCorrect = false
        }
        return FamilyGroupFormData(
            firstname = firstname,
            lastname = lastname,
            familyGroupName = familyGroupName,
            firstNameError = firstNameError,
            lastNameError = lastNameError,
            familyGroupNameError = familyGroupNameError,
            formIsCorrect = formIsCorrect
        )
    }
}


class FormValidator {

    fun validateEntry(formEntry: String): String? {
        if (formEntry.isEmpty())
        {
            return "Empty"
        }
        if (formEntry.length > 64) {
            return "Overfill"
        }
        return null
    }
}
