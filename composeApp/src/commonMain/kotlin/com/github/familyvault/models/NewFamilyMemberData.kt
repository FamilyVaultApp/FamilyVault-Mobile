package com.github.familyvault.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class NewFamilyMemberData(
    val memberIdentifier: MemberIdentifier,
    val keyPair: PublicEncryptedPrivateKeyPair
) {
    val id: String
        get() = Json.encodeToString(memberIdentifier)
}