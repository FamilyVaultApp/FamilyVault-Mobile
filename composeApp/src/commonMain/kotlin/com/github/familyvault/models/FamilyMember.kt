package com.github.familyvault.models

data class FamilyMember(
    val firstname: String,
    val surname: String,
    val publicKey: String
){
    val fullname: String
        get() = "$firstname $surname"
}