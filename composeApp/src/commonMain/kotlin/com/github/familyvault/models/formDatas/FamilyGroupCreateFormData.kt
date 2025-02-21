package com.github.familyvault.models.formDatas

data class FamilyGroupFormData(
    var firstname: String = "",
    var lastname: String = "",
    var familyGroupName: String = "",
) {
    val fullname: String
        get() = "$firstname $lastname"
}
