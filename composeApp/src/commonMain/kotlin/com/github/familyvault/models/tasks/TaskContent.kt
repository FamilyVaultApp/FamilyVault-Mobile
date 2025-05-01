package com.github.familyvault.models.tasks

import kotlinx.serialization.Serializable

@Serializable
data class TaskContent(
    val name: String,
    val description: String,
    val completed: Boolean,
    val assignedMemberId: String?
)
