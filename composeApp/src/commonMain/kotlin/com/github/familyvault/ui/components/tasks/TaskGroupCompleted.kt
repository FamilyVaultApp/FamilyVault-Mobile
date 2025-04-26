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
import androidx.compose.runtime.setValue
import com.github.familyvault.models.tasks.Task
import com.github.familyvault.ui.components.CollapseButton
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.tasks_completed
import org.jetbrains.compose.resources.stringResource

@Composable
fun TaskGroupCompleted(tasks: List<Task>) {
    var isCollapsed by remember { mutableStateOf(true) }
    val durationOfAnimation = 150

    TaskGroup(stringResource(Res.string.tasks_completed), primary = false, tasks = {
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
                tasks.map { TaskEntry(it, onCompletedChange = {}) }
            }
        }
    }, actionButton = {
        CollapseButton(
            isCollapsed, onClick = {
                isCollapsed = !isCollapsed
            })
    })
}
