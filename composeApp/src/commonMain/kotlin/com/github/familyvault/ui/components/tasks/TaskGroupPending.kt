package com.github.familyvault.ui.components.tasks

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.github.familyvault.models.tasks.Task
import com.github.familyvault.states.ITaskListState
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun TaskGroupPending(categoryTitle: String, tasks: List<Task>) {
    val taskListState = koinInject<ITaskListState>()

    val coroutineScope = rememberCoroutineScope()

    TaskGroup(
        title = categoryTitle,
        primary = true,
        tasks = {
            Column {
                tasks.forEach {
                    AnimatedTaskEntry(
                        it,
                        shouldAnimate = it.wasFetchedLater,
                        onCompletedClick = {
                            coroutineScope.launch {
                                taskListState.markTaskAsCompleted(it.id)
                            }
                        },
                    )
                }
            }
        },
        actionButton = {
            TaskGroupConfigurationButton { }
        }
    )
}