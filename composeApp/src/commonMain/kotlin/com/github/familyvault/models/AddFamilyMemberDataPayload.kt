package com.github.familyvault.models

import kotlinx.serialization.Serializable

@Serializable
data class AddFamilyMemberDataPayload(
    val newMemberData: NewFamilyMemberData,
    var joinStatusToken: String,
)