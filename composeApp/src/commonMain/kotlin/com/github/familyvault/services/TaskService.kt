package com.github.familyvault.services

import com.github.familyvault.AppConfig
import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.models.enums.tasks.TaskMessageContentType
import com.github.familyvault.models.enums.tasks.TaskThreadType
import com.github.familyvault.models.tasks.Task
import com.github.familyvault.models.tasks.TaskContent
import com.github.familyvault.models.tasks.TaskList
import com.github.familyvault.utils.FamilyMembersSplitter
import com.github.familyvault.utils.mappers.ThreadItemToTaskListMapper
import com.github.familyvault.utils.mappers.ThreadMessageItemToTask
import kotlinx.serialization.json.Json

class TaskService(
    private val familyGroupService: IFamilyGroupService,
    private val familyGroupSessionService: IFamilyGroupSessionService,
    private val privMxClient: IPrivMxClient
) : ITaskService {
    override suspend fun createNewTaskList(name: String) {
        val contextId = familyGroupSessionService.getContextId()

        val splitFamilyMembers = FamilyMembersSplitter.split(
            familyGroupService.retrieveFamilyGroupMembersList()
        )

        privMxClient.createThread(
            contextId,
            splitFamilyMembers.members.map { it.toPrivMxUser() },
            splitFamilyMembers.guardians.map { it.toPrivMxUser() },
            tag = AppConfig.TASK_THREAD_TAG,
            type = TaskThreadType.LIST.toString(),
            name = name,
            referenceStoreId = null,
            splitFamilyMembers.guardians.map { it.toPrivMxUser() }
        )
    }

    override suspend fun getTaskLists(): List<TaskList> {
        val contextId = familyGroupSessionService.getContextId()
        val tasksThreads =
            privMxClient.retrieveAllThreadsWithTag(contextId, AppConfig.TASK_THREAD_TAG, 0, 100)

        return tasksThreads.map { ThreadItemToTaskListMapper.map(it) }
    }

    override suspend fun createNewTask(taskListId: String, name: String, description: String) {
        val serializedTaskContent = Json.encodeToString(
            TaskContent(
                name, description, completed = false, assignedMemberPubKey = null
            )
        )

        privMxClient.sendMessage(
            taskListId, serializedTaskContent, TaskMessageContentType.TASK.toString()
        )
    }

    override suspend fun getTasksFromList(taskListId: String): List<Task> {
        // TODO: Implement task pagination
        val tasks = privMxClient.retrieveMessagesFromThread(
            taskListId,
            startIndex = 0,
            pageSize = 100,
        )

        return tasks.map {
            ThreadMessageItemToTask.map(
                it
            )
        }
    }

    override suspend fun updateTask(task: Task) {
        privMxClient.updateMessageContent(
            task.id, Json.encodeToString(task.content)
        )
    }
}