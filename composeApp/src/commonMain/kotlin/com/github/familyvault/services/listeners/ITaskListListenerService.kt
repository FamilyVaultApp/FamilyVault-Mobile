package com.github.familyvault.services.listeners

import com.github.familyvault.models.tasks.TaskList

interface ITaskListListenerService  : IListenerService {
    fun startListeningForTaskListThread(onNewTaskListThread: (TaskList) -> Unit)
    fun startListeningForUpdatedTaskListThread(onUpdatedTaskListThread: (TaskList) -> Unit)
}