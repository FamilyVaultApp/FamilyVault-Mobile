package com.github.familyvault.ui.screens.main.tasks.taskList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.models.enums.FamilyGroupMemberPermissionGroup
import com.github.familyvault.services.IFamilyGroupService
import com.github.familyvault.services.listeners.ITaskListenerService
import com.github.familyvault.states.ITaskListState
import com.github.familyvault.ui.components.HorizontalScrollableRow
import com.github.familyvault.ui.components.tasks.TaskGroupCompleted
import com.github.familyvault.ui.components.tasks.TaskGroupPending
import com.github.familyvault.ui.components.tasks.TaskListButton
import com.github.familyvault.ui.components.tasks.TaskNewListButton
import com.github.familyvault.ui.theme.AdditionalTheme
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun TaskListContent() {
    val localNavigator = LocalNavigator.currentOrThrow
    val familyGroupService = koinInject<IFamilyGroupService>()
    val taskListState = koinInject<ITaskListState>()
    val taskListenerService = koinInject<ITaskListenerService>()
    var currentUserPermissionGroup by remember { mutableStateOf(FamilyGroupMemberPermissionGroup.Guest)}
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        currentUserPermissionGroup = familyGroupService.retrieveMyFamilyMemberData().permissionGroup
    }

    LaunchedEffect(taskListState.selectedTaskList) {

        taskListState.selectedTaskList?.let { selected ->
            taskListenerService.unregisterAllListeners()
            taskListenerService.startListeningForNewTask(selected.id) {
                taskListState.addNewTask(it)
            }
            taskListenerService.startListeningForTaskUpdates(selected.id) {
                taskListState.updateTask(it)
            }
        }
    }

    LaunchedEffect(taskListState.taskLists) {
        if (taskListState.selectedTaskList == null) {
            taskListState.selectFirstTaskList()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            taskListenerService.unregisterAllListeners()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = AdditionalTheme.spacings.screenPadding)
    ) {
        HorizontalScrollableRow(
            modifier = Modifier.padding(horizontal = AdditionalTheme.spacings.screenPadding)
        ) {
            taskListState.taskLists.forEach {
                TaskListButton(
                    title = it.name,
                    selected = it == taskListState.selectedTaskList
                ) {
                    coroutineScope.launch {
                        taskListState.selectTaskList(it.id)
                    }
                }
            }
            if (currentUserPermissionGroup == FamilyGroupMemberPermissionGroup.Guardian) {
                TaskNewListButton {
                    localNavigator.parent?.push(TaskNewListScreen())
                }
            }
        }

        Spacer(modifier = Modifier.height(AdditionalTheme.spacings.screenPadding))

        Column(
            verticalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.medium),
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = AdditionalTheme.spacings.screenPadding)
                .verticalScroll(scrollState)
        ) {
            taskListState.selectedTaskList?.let { taskList ->
                TaskGroupPending(
                    categoryTitle = taskList.name,
                    tasks = taskListState.tasks.filter { !it.content.completed },
                    onEditClick = {
                        localNavigator.parent?.push(TaskListEditScreen(taskList))
                    },
                    permissionGroup = currentUserPermissionGroup
                )
                TaskGroupCompleted(
                    tasks = taskListState.tasks.filter { it.content.completed }
                )
            }
            Spacer(modifier = Modifier.height(AdditionalTheme.spacings.screenPadding))
        }
    }
}
