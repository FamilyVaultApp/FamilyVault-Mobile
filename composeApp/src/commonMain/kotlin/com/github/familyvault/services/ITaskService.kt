package com.github.familyvault.services

import com.github.familyvault.models.tasks.Task
import com.github.familyvault.models.tasks.TaskList

interface ITaskService {
    suspend fun createNewTaskList(name: String)
    suspend fun getTaskLists(): List<TaskList>
    suspend fun createNewTask(taskListId: String, name: String, description: String)
    suspend fun getTasksFromList(taskListId: String): List<Task>
    suspend fun updateTask(task: Task)
}