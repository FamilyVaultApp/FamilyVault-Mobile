package com.github.familyvault.models

import com.github.familyvault.backend.models.PrivMxUser
import com.github.familyvault.models.enums.FamilyGroupMemberPermissionGroup
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class FamilyMember(
    val identifier: MemberIdentifier,
    val publicKey: String,
    val permissionGroup: FamilyGroupMemberPermissionGroup
) {
    val firstname: String
        get() = identifier.firstname

    val surname: String?
        get() = identifier.surname

    val fullname: String
        get() = identifier.fullname

    val id: String
        get() = Json.encodeToString(identifier)

    fun toPrivMxUser() = PrivMxUser(id, publicKey)

    override fun equals(other: Any?): Boolean {
        if (other !is FamilyMember) {
            return false
        }
        return other.id == id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}