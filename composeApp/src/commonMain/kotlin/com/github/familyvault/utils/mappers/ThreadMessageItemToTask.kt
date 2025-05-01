package com.github.familyvault.utils.mappers

import com.github.familyvault.backend.models.ThreadMessageItem
import com.github.familyvault.models.tasks.Task
import kotlinx.serialization.json.Json

object ThreadMessageItemToTask {
    fun map(threadMessageItem: ThreadMessageItem): Task = Task(
        id = threadMessageItem.messageId,
        content = Json.decodeFromString(requireNotNull(threadMessageItem.messageContent))
    )
}