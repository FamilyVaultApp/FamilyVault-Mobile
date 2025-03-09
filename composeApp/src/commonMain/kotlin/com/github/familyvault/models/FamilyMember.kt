package com.github.familyvault.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FamilyMember(
    val firstname: String,
    val surname: String,
    val publicKey: String,
    val permissionGroup: FamilyGroupMemberPermissionGroup
){
    val fullname: String
        get() = "$firstname $surname"
}


@Serializable
enum class FamilyGroupMemberPermissionGroup(val value: Int) {
    @SerialName("0") Guardian(0),
    @SerialName("1") Member(1),
    @SerialName("2") Guest(2);
}