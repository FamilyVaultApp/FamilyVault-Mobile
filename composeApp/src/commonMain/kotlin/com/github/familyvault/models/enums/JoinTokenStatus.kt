package com.github.familyvault.models.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class JoinTokenStatus(val value: Int) {
    @SerialName("0") Pending(0),
    @SerialName("1") Success(1),
    @SerialName("2") Error(2)
}
