package com.github.familyvault.ui.screens.main.tasks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.github.familyvault.services.listeners.ITaskListListenerService
import com.github.familyvault.states.IFamilyMembersState
import com.github.familyvault.states.ITaskListState
import com.github.familyvault.ui.components.LoaderWithText
import com.github.familyvault.ui.screens.main.tasks.taskList.TaskListContent
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.loading
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun TaskTabContent() {
    val tasksCategoriesState = koinInject<ITaskListState>()
    val familyMembersState = koinInject<IFamilyMembersState>()
    val tasksListState = koinInject<ITaskListState>()
    val taskListListenerService = koinInject<ITaskListListenerService>()

    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        isLoading = true
        tasksCategoriesState.populateTaskListFromServices()
        familyMembersState.populateFamilyGroupMembersFromService()

        taskListListenerService.startListeningForNewTaskList { newList ->
            tasksListState.taskLists.add(newList)
        }
        taskListListenerService.startListeningForUpdatedTaskList { updatedList ->
            tasksListState.updateTaskList(updatedList)
        }

        isLoading = false
    }

    DisposableEffect(Unit) {
        onDispose {
            taskListListenerService.unregisterAllListeners()
        }
    }

    Column {
        if (isLoading) {
            LoaderWithText(
                stringResource(Res.string.loading), modifier = Modifier.fillMaxSize()
            )
        } else {
            if (tasksListState.isEmpty()) {
                TasksNoCategoriesContent()
            } else {
                TaskListContent()
            }
        }
    }
}