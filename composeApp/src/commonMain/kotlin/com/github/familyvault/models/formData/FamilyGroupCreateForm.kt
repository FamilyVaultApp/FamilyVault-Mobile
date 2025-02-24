package com.github.familyvault.models.formData

class FamilyGroupFormData(
    var firstname: FormDataEntry<String> = FormDataEntry(""),
    var lastname: FormDataEntry<String> = FormDataEntry(""),
    var familyGroupName: FormDataEntry<String> = FormDataEntry(""),

    var formIsCorrect: Boolean = false,

    var editedFields: MutableSet<String> = mutableSetOf()
) {
    val fullname: String
        get() = "$firstname $lastname"


    private fun validateCreateFamilyGroupFormEntry(formEntry: String): String? {
        if (formEntry.isEmpty())
        {
            return "Empty"
        }
        if (formEntry.length >= 64) {
            return "Overfill"
        }
        return null
    }

    fun validateForm(): FamilyGroupFormData {
        formIsCorrect = true

        if (editedFields.contains("firstname")) validateField(firstname, "firstname")
        if (editedFields.contains("lastname")) validateField(lastname, "lastname")
        if (editedFields.contains("familyGroupName")) validateField(familyGroupName, "familyGroupName")

        return this
    }

    private fun validateField(formEntry: FormDataEntry<String>, fieldName: String) {
        val error = validateCreateFamilyGroupFormEntry(formEntry.value)

        if (error != null) {
            formEntry.validationError = error
            formIsCorrect = false
            editedFields.add(fieldName)
        } else {
            formEntry.validationError = null
        }
    }

}

data class FormDataEntry<T>(
    var value: T,
    var validationError: String? = null
)

