package com.github.familyvault.models.formDatas

data class FamilyGroupFormData(
    var firstname: String = "",
    var lastname: String = "",
    var familyGroupName: String = "",

    var firstNameError: String? = "",
    var lastNameError: String? = "",
    var familyGroupNameError: String? = "",
    var formIsCorrect: Boolean = false,

    var editedFields: MutableSet<String> = mutableSetOf()
) {
    val fullname: String
        get() = "$firstname $lastname"

    private val validator = FormValidator()

    fun validateForm(): FamilyGroupFormData {
        return copy(
            firstNameError = if ("firstname" in editedFields) validator.validateEntry(firstname) else null,
            lastNameError = if ("lastname" in editedFields) validator.validateEntry(lastname) else null,
            familyGroupNameError = if ("familyGroupName" in editedFields) validator.validateEntry(familyGroupName) else null,
            formIsCorrect = editedFields.containsAll(listOf("firstname", "lastname", "familyGroupName")) &&
                    listOf(firstNameError, lastNameError, familyGroupNameError).all { it.isNullOrBlank() }
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
