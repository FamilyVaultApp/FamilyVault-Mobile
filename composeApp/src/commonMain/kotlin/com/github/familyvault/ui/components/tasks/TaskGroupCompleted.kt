package com.github.familyvault.ui.components.tasks

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.github.familyvault.models.tasks.Task
import com.github.familyvault.states.ITaskListState
import com.github.familyvault.ui.components.CollapseButton
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.task_completed
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun TaskGroupCompleted(tasks: List<Task>) {
    val taskListState = koinInject<ITaskListState>()

    var isCollapsed by remember { mutableStateOf(true) }
    val durationOfAnimation = 150
    val coroutineScope = rememberCoroutineScope()

    TaskGroup(stringResource(Res.string.task_completed), primary = false, tasks = {
        AnimatedVisibility(
            visible = !isCollapsed,
            enter = fadeIn(tween(durationMillis = durationOfAnimation)) + expandVertically(
                tween(
                    durationMillis = durationOfAnimation
                )
            ),
            exit = fadeOut(tween(durationMillis = durationOfAnimation)) + shrinkVertically(
                tween(
                    durationMillis = durationOfAnimation
                )
            )
        ) {
            Column {
                tasks.forEach {
                    TaskEntry(it, onCompletedClick = {
                        coroutineScope.launch {
                            taskListState.markTaskAsIncomplete(it.id)
                        }
                    })
                }
            }
        }
    }, actionButton = {
        CollapseButton(
            isCollapsed, onClick = {
                isCollapsed = !isCollapsed
            })
    })
}
