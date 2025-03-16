package com.github.familyvault.models.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class FamilyGroupMemberPermissionGroup(val value: Int) {
    @SerialName("0") Guardian(0),
    @SerialName("1") Member(1),
    @SerialName("2") Guest(2);
}