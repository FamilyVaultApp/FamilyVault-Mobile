package com.github.familyvault.services.listeners

import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.models.tasks.TaskList

class TaskListListenerService(
    private val privMxClient: IPrivMxClient
) : ITaskListListenerService {
    companion object {
        const val CREATE_EVENT_NAME = "TASK_LIST_THREAD_CREATE"
        const val UPDATE_EVENT_NAME = "TASK_LIST_THREAD_UPDATE"
    }

    override fun startListeningForTaskList(onNewTaskList: (TaskList) -> Unit) {
        privMxClient.registerOnThreadCreated(CREATE_EVENT_NAME) {
            onNewTaskList(
                TaskList(
                    id = it.threadId,
                    name = it.privateMeta.name
                )
            )
        }
    }

    override fun startListeningForUpdatedTaskList(onUpdatedTaskList: (TaskList) -> Unit) {
        privMxClient.registerOnThreadUpdated(UPDATE_EVENT_NAME) {
            onUpdatedTaskList(
                TaskList(
                    id = it.threadId,
                    name = it.privateMeta.name
                )
            )
        }
    }

    override fun unregisterAllListeners() {
        privMxClient.unregisterAllEvents(CREATE_EVENT_NAME)
        privMxClient.unregisterAllEvents(UPDATE_EVENT_NAME)
    }
}