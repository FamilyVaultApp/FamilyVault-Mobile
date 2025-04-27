package com.github.familyvault.utils.mappers

import com.github.familyvault.backend.models.ThreadItem
import com.github.familyvault.models.tasks.TaskList

object ThreadItemToTaskListMapper {
    fun map(threadItem: ThreadItem): TaskList = TaskList(
        id = threadItem.threadId,
        name = threadItem.privateMeta.name
    )
}