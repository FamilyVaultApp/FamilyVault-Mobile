package com.github.familyvault.ui.components.tasks

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.models.tasks.Task
import com.github.familyvault.states.ITaskListState
import com.github.familyvault.ui.screens.main.tasks.task.TaskEditScreen
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun TaskGroupPending(categoryTitle: String, tasks: List<Task>, onEditClick: () -> Unit) {
    val localNavigator = LocalNavigator.currentOrThrow
    val taskListState = koinInject<ITaskListState>()

    val coroutineScope = rememberCoroutineScope()
    var taskToAssignMember by remember { mutableStateOf<Task?>(null) }

    TaskGroup(
        title = categoryTitle,
        primary = true,
        tasks = {
            Column {
                tasks.sortedBy { it.createDate }.reversed().forEach {
                    AnimatedTaskEntry(
                        it,
                        shouldAnimate = it.wasFetchedLater,
                        onCompletedClick = { task ->
                            coroutineScope.launch {
                                taskListState.markTaskAsCompleted(task.id)
                            }
                        },
                        onEditClick = { task ->
                            localNavigator.parent?.push(TaskEditScreen(task))
                        },
                        onAssignClick = { task ->
                            taskToAssignMember = task
                        }
                    )
                }
            }
        },
        actionButton = {
            TaskGroupConfigurationButton(
                onClick = onEditClick
            )
        }
    )

    taskToAssignMember?.let {
        AssignMemberToTaskBottomSheet(
            it,
            onDismissRequest = {
                taskToAssignMember = null
            }
        )
    }
}