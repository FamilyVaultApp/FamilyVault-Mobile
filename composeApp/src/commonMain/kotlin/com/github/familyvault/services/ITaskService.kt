package com.github.familyvault.services

import com.github.familyvault.models.tasks.TaskList

interface ITaskService {
    suspend fun createNewTaskList(name: String)
    suspend fun getTaskLists(): List<TaskList>
}