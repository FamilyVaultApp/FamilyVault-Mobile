package com.github.familyvault.services.listeners

import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.models.tasks.Task
import com.github.familyvault.utils.mappers.ThreadMessageItemToTask

class TaskListenerService(
    private val privMxClient: IPrivMxClient
) : ITaskListenerService {
    companion object {
        const val CREATE_EVENT_NAME = "TASK_CREATE"
        const val UPDATE_EVENT_NAME = "TASK_UPDATE"
    }

    override fun startListeningForNewTask(taskListId: String, onNewTask: (Task) -> Unit) {
        privMxClient.registerOnMessageCreated(CREATE_EVENT_NAME, taskListId) {
            onNewTask(
                ThreadMessageItemToTask.map(it, wasFetchedLater = true)
            )
        }

    }

    override fun startListeningForTaskUpdates(taskListId: String, onUpdateTask: (Task) -> Unit) {
        privMxClient.registerOnMessageUpdate(UPDATE_EVENT_NAME, taskListId) {
            onUpdateTask(
                ThreadMessageItemToTask.map(it, wasFetchedLater = true)
            )
        }
    }

    override fun unregisterAllListeners() {
        privMxClient.unregisterAllEvents(CREATE_EVENT_NAME)
        privMxClient.unregisterAllEvents(UPDATE_EVENT_NAME)
    }
}