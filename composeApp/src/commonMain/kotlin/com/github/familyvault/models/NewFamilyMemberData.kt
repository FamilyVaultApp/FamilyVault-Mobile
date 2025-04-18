package com.github.familyvault.models

import kotlinx.serialization.Serializable

@Serializable
data class NewFamilyMemberData(
    val firstname: String,
    val surname: String,
    val keyPair: PublicEncryptedPrivateKeyPair
) {
    val fullname: String
        get() = "$firstname $surname"
}