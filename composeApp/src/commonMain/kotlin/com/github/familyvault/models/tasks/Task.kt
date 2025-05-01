package com.github.familyvault.models.tasks

data class Task(val id: String, val content: TaskContent, val wasFetchedLater: Boolean = false)