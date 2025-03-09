package com.github.familyvault.models

import kotlinx.serialization.Serializable

@Serializable
data class FamilyMember(
    val firstname: String,
    val surname: String,
    val publicKey: String,
    val permissionGroup: Int
){
    val fullname: String
        get() = "$firstname $surname"
}