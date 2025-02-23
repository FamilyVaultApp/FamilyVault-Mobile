package com.github.familyvault.models.formData

data class FamilyGroupFormData(
    var firstname: String = "",
    var lastname: String = "",
    var familyGroupName: String = "",
) {
    val fullname: String
        get() = "$firstname $lastname"
}
