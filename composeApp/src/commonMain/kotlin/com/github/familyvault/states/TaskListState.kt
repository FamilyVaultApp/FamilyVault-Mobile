package com.github.familyvault.states

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.github.familyvault.backend.models.ThreadId
import com.github.familyvault.models.tasks.Task
import com.github.familyvault.models.tasks.TaskList
import com.github.familyvault.services.ITaskService

class TaskListState(private val tasksService: ITaskService) : ITaskListState {
    override val taskLists: MutableList<TaskList> = mutableStateListOf()
    override var selectedTaskList: TaskList? by mutableStateOf(null)
        private set
    override var tasks: MutableList<Task> = mutableStateListOf()
        private set

    override fun addNewTask(task: Task) {
        tasks.add(task)
    }

    override fun updateTask(task: Task) {
        val index = tasks.indexOfFirst { it.id == task.id }
        if (index != -1) {
            tasks[index] = task
        } else {
            tasks.add(task)
        }
    }

    override suspend fun populateTaskListFromServices() {
        taskLists.clear()
        taskLists.addAll(tasksService.getTaskLists())
    }

    override fun updateTaskList(taskList: TaskList) {
        taskLists.removeAll { it.id == taskList.id }
        taskLists.add(taskList)
    }

    override suspend fun removeTaskList(taskListId: String) {
        taskLists.removeAll { it.id == taskListId }

        if (selectedTaskList?.id == taskListId) {
            selectFirstTaskList()
        }
    }

    override suspend fun populateTaskFormTaskListFromServices() {
        selectedTaskList?.let {
            tasks.clear()
            tasks.addAll(tasksService.getTasksFromList(it.id))
        }
    }

    override fun isEmpty(): Boolean {
        return taskLists.isEmpty()
    }

    override suspend fun selectTaskList(id: String) {
        selectedTaskList = taskLists.firstOrNull { it.id == id }
        populateTaskFormTaskListFromServices()
    }

    override suspend fun selectFirstTaskList() {
        selectedTaskList = taskLists.firstOrNull()
        selectedTaskList?.let {
            populateTaskFormTaskListFromServices()
        }
    }

    override fun unselectTaskList() {
        selectedTaskList = null
        tasks.clear()
    }

    override suspend fun markTaskAsCompleted(taskId: String) =
        changeTaskCompleteStatus(taskId, completeStatus = true)

    override suspend fun markTaskAsIncomplete(taskId: String) =
        changeTaskCompleteStatus(taskId, completeStatus = false)

    private suspend fun changeTaskCompleteStatus(taskId: String, completeStatus: Boolean) {
        val task = tasks.first { it.id == taskId }

        tasksService.updateTask(task.id, task.content.copy(completed = completeStatus))
    }
}