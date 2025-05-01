package com.github.familyvault.services.listeners

import com.github.familyvault.models.tasks.Task

interface ITaskListenerService : IListenerService {
    fun startListeningForNewTask(taskListId: String, onNewTask: (Task) -> Unit)
    fun startListeningForTaskUpdates(taskListId: String, onUpdateTask: (Task) -> Unit)
}