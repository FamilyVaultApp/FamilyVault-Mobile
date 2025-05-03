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

    override fun startListeningForTaskListThread(onNewTaskListThread: (TaskList) -> Unit) {
        privMxClient.registerOnThreadCreated(CREATE_EVENT_NAME) {
            onNewTaskListThread(
                TaskList(
                    id = it.threadId,
                    name = it.privateMeta.name
                )
            )
        }
    }

    override fun startListeningForUpdatedTaskListThread(onUpdatedTaskListThread: (TaskList) -> Unit) {
        privMxClient.registerOnThreadUpdated(UPDATE_EVENT_NAME) {
            onUpdatedTaskListThread(
                TaskList(
                    id = it.threadId,
                    name = it.privateMeta.name
                )
            )
        }
    }

    override fun unregisterAllListeners() {
        privMxClient.unregisterAllEvents(ChatThreadListenerService.CREATE_EVENT_NAME)
        privMxClient.unregisterAllEvents(ChatThreadListenerService.UPDATE_EVENT_NAME)
    }
}