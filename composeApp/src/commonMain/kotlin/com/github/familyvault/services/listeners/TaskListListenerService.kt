package com.github.familyvault.services.listeners

import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.backend.models.ThreadId
import com.github.familyvault.models.tasks.TaskList
import com.github.familyvault.utils.mappers.ThreadItemToTaskListMapper

class TaskListListenerService(
    private val privMxClient: IPrivMxClient
) : ITaskListListenerService {
    companion object {
        const val CREATE_EVENT_NAME = "TASK_LIST_THREAD_CREATE"
        const val UPDATE_EVENT_NAME = "TASK_LIST_THREAD_UPDATE"
        const val DELETE_EVENT_NAME = "TASK_LIST_THREAD_DELETE"
    }

    override fun startListeningForNewTaskList(onNewTaskList: (TaskList) -> Unit) {
        privMxClient.registerOnThreadCreated(CREATE_EVENT_NAME) {
            onNewTaskList(
                ThreadItemToTaskListMapper.map(it)
            )
        }
    }

    override fun startListeningForUpdatedTaskList(onUpdatedTaskList: (TaskList) -> Unit) {
        privMxClient.registerOnThreadUpdated(UPDATE_EVENT_NAME) {
            onUpdatedTaskList(
                ThreadItemToTaskListMapper.map(it)
            )
        }
    }

    override fun startListeningForDeletedTaskList(onDeletedTaskList: (ThreadId) -> Unit) {
        privMxClient.registerOnThreadDeleted(DELETE_EVENT_NAME) {
            onDeletedTaskList(
                it
            )
        }
    }


    override fun unregisterAllListeners() {
        privMxClient.unregisterAllEvents(CREATE_EVENT_NAME)
        privMxClient.unregisterAllEvents(UPDATE_EVENT_NAME)
    }
}