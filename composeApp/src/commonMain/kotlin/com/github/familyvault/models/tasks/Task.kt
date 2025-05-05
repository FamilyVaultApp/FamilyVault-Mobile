package com.github.familyvault.models.tasks

import kotlinx.datetime.LocalDateTime

data class Task(
    val id: String,
    val content: TaskContent,
    val createDate: LocalDateTime,
    val wasFetchedLater: Boolean = false
)