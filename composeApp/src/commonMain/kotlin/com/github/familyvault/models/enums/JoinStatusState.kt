package com.github.familyvault.models.enums

import com.github.familyvault.utils.EnumSerializer
import kotlinx.serialization.Serializable

private class JoinStatusStateSerializer : EnumSerializer<JoinStatusState>(
    "JoinStatusState",
    { it.value },
    { v -> JoinStatusState.entries.first { it.value == v } }
)

@Serializable(with = JoinStatusStateSerializer::class)
enum class JoinStatusState(val value: Int) {
    Initiated(0),
    Pending(1),
    Success(2),
    Error(3)
}
