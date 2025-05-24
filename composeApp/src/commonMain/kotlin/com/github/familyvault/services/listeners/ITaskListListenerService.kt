package com.github.familyvault.services.listeners

import com.github.familyvault.backend.models.ThreadId
import com.github.familyvault.models.tasks.TaskList

interface ITaskListListenerService : IListenerService {
    fun startListeningForNewTaskList(onNewTaskList: (TaskList) -> Unit)
    fun startListeningForUpdatedTaskList(onUpdatedTaskList: (TaskList) -> Unit)
    fun startListeningForDeletedTaskList(onDeletedTaskList: (ThreadId) -> Unit)
}