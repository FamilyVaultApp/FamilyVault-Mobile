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
        var firstnameIsCorrect = false;
        var lastnameIsCorrect = false;
        var familyGroupNameIsCorrect = false;
        if (editedFields.contains("firstname")) firstnameIsCorrect = validateField(firstname)
        if (editedFields.contains("lastname")) lastnameIsCorrect = validateField(lastname)
        if (editedFields.contains("familyGroupName")) familyGroupNameIsCorrect = validateField(familyGroupName)
        formIsCorrect = firstnameIsCorrect && lastnameIsCorrect && familyGroupNameIsCorrect
        return this
    }

    private fun validateField(formEntry: FormDataEntry<String>): Boolean {
        val error = validateCreateFamilyGroupFormEntry(formEntry.value)

        if (error != null) {
            formEntry.validationError = error
            return false
        } else {
            formEntry.validationError = null
            return true
        }
    }

}

data class FormDataEntry<T>(
    var value: T,
    var validationError: String? = null
)

