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
            threadIcon = null,
            splitFamilyMembers.guardians.map { it.toPrivMxUser() },
        )
    }

    override suspend fun updateTaskList(taskListId: String, name: String) {
        val splitFamilyMembers = FamilyMembersSplitter.split(
            familyGroupService.retrieveFamilyGroupMembersList()
        )

        privMxClient.updateThread(
            taskListId,
            users = splitFamilyMembers.members.map { it.toPrivMxUser() },
            managers = splitFamilyMembers.guardians.map { it.toPrivMxUser() },
            newName = name,
        )
    }

    override suspend fun deleteTaskList(taskListId: String) {
        privMxClient.deleteThread(taskListId)
    }

    override suspend fun getTaskLists(): List<TaskList> {
        val contextId = familyGroupSessionService.getContextId()
        val tasksThreads =
            privMxClient.retrieveAllThreadsWithTag(contextId, AppConfig.TASK_THREAD_TAG, 0, 100)

        return tasksThreads.map { ThreadItemToTaskListMapper.map(it) }
    }

    override suspend fun createNewTask(
        taskListId: String,
        title: String,
        description: String,
        assignedMemberPubKey: String?
    ) {
        createNewTask(
            taskListId,
            TaskContent(title, description, completed = false, assignedMemberPubKey)
        )
    }

    override suspend fun createNewTask(taskListId: String, content: TaskContent) {
        val serializedTaskContent = Json.encodeToString(content)

        privMxClient.sendMessage(
            taskListId, serializedTaskContent, TaskMessageContentType.TASK.toString()
        )
    }

    override suspend fun updateTask(
        taskId: String,
        title: String?,
        description: String?,
        assignedMemberPubKey: String?
    ) {
        val taskContentString = privMxClient.retrieveMessageById(taskId).messageContent
        val taskContent = Json.decodeFromString<TaskContent>(requireNotNull(taskContentString))
        val modifiedTaskContent = taskContent.copy(
            title = title ?: taskContent.title,
            description = description ?: taskContent.description,
            assignedMemberPubKey = assignedMemberPubKey ?: taskContent.assignedMemberPubKey
        )

        updateTask(taskId, modifiedTaskContent)
    }

    override suspend fun updateTask(taskId: String, content: TaskContent) {
        privMxClient.updateMessageContent(
            taskId, Json.encodeToString(content)
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

    override suspend fun restoreTaskListsMembership() {
        val taskLists = getTaskLists()
        val splitFamilyMembers =
            FamilyMembersSplitter.split(familyGroupService.retrieveFamilyGroupMembersList())

        for (taskList in taskLists) {
            privMxClient.updateThread(
                taskList.id,
                users = splitFamilyMembers.members.map { it.toPrivMxUser() },
                managers = splitFamilyMembers.guardians.map { it.toPrivMxUser() },
            )
        }
    }

}