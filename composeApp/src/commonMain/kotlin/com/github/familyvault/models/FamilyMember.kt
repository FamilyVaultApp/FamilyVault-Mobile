package com.github.familyvault.models

import com.github.familyvault.backend.models.PrivMxUser
import com.github.familyvault.models.enums.FamilyGroupMemberPermissionGroup
import kotlinx.serialization.Serializable

@Serializable
data class FamilyMember(
    val firstname: String,
    val surname: String,
    val publicKey: String,
    val permissionGroup: FamilyGroupMemberPermissionGroup
) {
    val fullname: String
        get() = "$firstname $surname"

    val id: String
        get() = fullname

    fun toPrivMxUser() = PrivMxUser(fullname, publicKey)
}