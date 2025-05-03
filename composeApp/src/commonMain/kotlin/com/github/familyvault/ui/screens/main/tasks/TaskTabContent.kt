package com.github.familyvault.ui.screens.main.tasks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.github.familyvault.services.listeners.ITaskListListenerService
import com.github.familyvault.states.ITaskListState
import com.github.familyvault.ui.components.LoaderWithText
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.loading
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun TaskTabContent() {
    val tasksCategoriesState = koinInject<ITaskListState>()
    val taskListListenerService = koinInject<ITaskListListenerService>()

    var isLoading by remember { mutableStateOf(true) }
    var isTaskListEmpty by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        isLoading = true
        tasksCategoriesState.populateTaskListFromServices()
        if (tasksCategoriesState.isEmpty())
        {
            taskListListenerService.startListeningForTaskListThread {
                isTaskListEmpty = false
            }
            taskListListenerService.unregisterAllListeners()
        } else {
            isTaskListEmpty = false
        }
        isLoading = false
    }

    Column {
        if (isLoading) {
            LoaderWithText(
                stringResource(Res.string.loading), modifier = Modifier.fillMaxSize()
            )
        } else {
            if (isTaskListEmpty) {
                TasksNoCategoriesContent()
            } else {
                TaskListContent()
            }
        }
    }
}