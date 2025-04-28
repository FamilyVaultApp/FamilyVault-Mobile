package com.github.familyvault.states

import androidx.compose.runtime.mutableStateListOf
import com.github.familyvault.models.tasks.TaskList
import com.github.familyvault.services.ITaskService

class TaskListState(private val tasksService: ITaskService) : ITaskListState {
    override val taskLists: MutableList<TaskList> = mutableStateListOf()

    override suspend fun populateFromServices() {
        taskLists.clear()
        taskLists.addAll(tasksService.getTaskLists())
    }

    override fun isEmpty(): Boolean {
        return taskLists.isEmpty()
    }
}