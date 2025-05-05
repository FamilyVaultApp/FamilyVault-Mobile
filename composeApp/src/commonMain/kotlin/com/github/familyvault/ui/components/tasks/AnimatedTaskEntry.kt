package com.github.familyvault.ui.components.tasks

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.github.familyvault.models.tasks.Task

@Composable
fun AnimatedTaskEntry(
    task: Task,
    onCompletedClick: (Task) -> Unit,
    onEditClick: (Task) -> Unit,
    onAssignClick: (Task) -> Unit,
    shouldAnimate: Boolean
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    AnimatedVisibility(
        visible = true,
        enter = if (shouldAnimate) fadeIn() + expandVertically() else EnterTransition.None,
        exit = ExitTransition.None
    ) {
        if (visible) {
            TaskEntry(task, onCompletedClick, onEditClick, onAssignClick)
        }
    }

}