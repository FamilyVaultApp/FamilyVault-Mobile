package com.github.familyvault.ui.screens.main.tasks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.models.tasks.Task
import com.github.familyvault.models.tasks.TaskList
import com.github.familyvault.states.ITaskListState
import com.github.familyvault.ui.components.HorizontalScrollableRow
import com.github.familyvault.ui.components.tasks.TaskGroupCompleted
import com.github.familyvault.ui.components.tasks.TaskGroupPending
import com.github.familyvault.ui.components.tasks.TaskListButton
import com.github.familyvault.ui.components.tasks.TaskNewListButton
import com.github.familyvault.ui.theme.AdditionalTheme
import org.koin.compose.koinInject

@Composable
fun TaskListContent() {
    val localNavigator = LocalNavigator.currentOrThrow
    val taskListState = koinInject<ITaskListState>()

    var selectedTaskList by remember { mutableStateOf<TaskList?>(null) }

    LaunchedEffect(taskListState.taskLists) {
        if (selectedTaskList == null) {
            selectedTaskList = taskListState.taskLists.firstOrNull()
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
                    title = it.name, selected = it == selectedTaskList
                ) {
                    selectedTaskList = it
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
            TaskGroupPending(
                selectedTaskList?.name ?: "", listOf(
                    Task(
                        id = "1", name = "Milk", description = "Description", completed = false
                    ), Task(
                        id = "2", name = "Egg", description = "Description", completed = false
                    )
                )
            )
            TaskGroupCompleted(
                listOf(
                    Task(
                        id = "3", name = "Flour", description = "Description", completed = true
                    ), Task(
                        id = "4", name = "Oil", description = "Description", completed = true
                    ), Task(
                        id = "5", name = "Petrol", description = "Description", completed = true
                    )
                )
            )
        }
    }
}