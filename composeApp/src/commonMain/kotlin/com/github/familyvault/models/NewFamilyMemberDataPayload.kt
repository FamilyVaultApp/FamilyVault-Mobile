package com.github.familyvault.models

import kotlinx.serialization.Serializable

@Serializable
data class NewFamilyMemberDataPayload(
    val newMemberData: NewFamilyMemberData,
    var joinStatus: FamilyMemberJoinStatus?
)