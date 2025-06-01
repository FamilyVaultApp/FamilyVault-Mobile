package com.github.familyvault.services

import com.github.familyvault.models.tasks.Task
import com.github.familyvault.models.tasks.TaskContent
import com.github.familyvault.models.tasks.TaskList

interface ITaskService {
    suspend fun getTaskLists(): List<TaskList>
    suspend fun createNewTaskList(name: String)
    suspend fun updateTaskList(taskListId: String, name: String): Boolean
    suspend fun deleteTaskList(taskListId: String)

    suspend fun createNewTask(
        taskListId: String,
        title: String,
        description: String,
        assignedMemberPubKey: String? = null
    )

    suspend fun createNewTask(
        taskListId: String,
        content: TaskContent
    )

    suspend fun updateTask(
        taskId: String,
        title: String? = null,
        description: String? = null,
        assignedMemberPubKey: String? = null
    )

    suspend fun updateTask(taskId: String, content: TaskContent)
    suspend fun getTasksFromList(taskListId: String): List<Task>

    suspend fun restoreTaskListsMembership()
}