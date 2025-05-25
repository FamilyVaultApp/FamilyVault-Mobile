package com.github.familyvault.states

import com.github.familyvault.backend.models.ThreadId
import com.github.familyvault.models.tasks.Task
import com.github.familyvault.models.tasks.TaskList

interface ITaskListState {
    val taskLists: MutableList<TaskList>
    val selectedTaskList: TaskList?
    val tasks: List<Task>

    fun addNewTask(task: Task)
    fun updateTask(task: Task)
    fun isEmpty(): Boolean
    fun unselectTaskList()
    fun updateTaskList(taskList: TaskList)
    suspend fun removeTaskList(taskListId: String)
    suspend fun markTaskAsCompleted(taskId: String)
    suspend fun markTaskAsIncomplete(taskId: String)
    suspend fun populateTaskListFromServices()
    suspend fun populateTaskFormTaskListFromServices()
    suspend fun selectFirstTaskList()
    suspend fun selectTaskList(id: String)
}