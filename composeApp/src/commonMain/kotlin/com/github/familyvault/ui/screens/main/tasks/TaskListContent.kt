package com.github.familyvault.ui.screens.main.tasks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
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
    val taskListState = koinInject<ITaskListState>()
    val taskListenerService = koinInject<ITaskListenerService>()

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(taskListState.selectedTaskList) {
        if (taskListState.selectedTaskList == null) {
            return@LaunchedEffect
        }

        val selectedTaskId = requireNotNull(taskListState.selectedTaskList).id
        taskListenerService.unregisterAllListeners()

        taskListenerService.startListeningForNewTask(selectedTaskId) {
            taskListState.addNewTask(it)
        }
        taskListenerService.startListeningForTaskUpdates(selectedTaskId) {
            taskListState.updateTask(it)
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
            taskListState.unselectTaskList()
        }
    }

    Column(
        modifier = Modifier.padding(vertical = AdditionalTheme.spacings.screenPadding)
    ) {
        HorizontalScrollableRow(
            modifier = Modifier.padding(horizontal = AdditionalTheme.spacings.screenPadding)
        ) {
            taskListState.taskLists.forEach {
                TaskListButton(
                    title = it.name, selected = it == taskListState.selectedTaskList
                ) {
                    coroutineScope.launch {
                        taskListState.selectTaskList(it.id)
                    }
                }
            }
            TaskNewListButton {
                localNavigator.parent?.push(TaskNewListScreen())
            }
        }
        Column(
            modifier = Modifier.padding(AdditionalTheme.spacings.screenPadding),
            verticalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.medium)
        ) {
            if (taskListState.selectedTaskList != null) {
                TaskGroupPending(
                    taskListState.selectedTaskList!!.name,
                    taskListState.tasks.filter { !it.content.completed }
                )
                TaskGroupCompleted(
                    taskListState.tasks.filter { it.content.completed }
                )
            }
        }
    }
}