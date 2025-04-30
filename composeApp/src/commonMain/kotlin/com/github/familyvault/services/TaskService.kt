package com.github.familyvault.services

import com.github.familyvault.AppConfig
import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.models.enums.tasks.TaskThreadType
import com.github.familyvault.models.tasks.TaskList
import com.github.familyvault.utils.FamilyMembersSplitter
import com.github.familyvault.utils.mappers.ThreadItemToTaskListMapper

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
}