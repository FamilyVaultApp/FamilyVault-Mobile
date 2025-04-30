package com.github.familyvault.states

import com.github.familyvault.models.tasks.TaskList

interface ITaskListState {
    val taskLists: MutableList<TaskList>

    suspend fun populateFromServices()
    fun isEmpty(): Boolean
}