package com.github.familyvault.ui.components.tasks

import androidx.compose.animation.AnimatedVisibility
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
fun AnimatedTaskEntry(task: Task, onCompletedClick: (Task) -> Unit, shouldAnimate: Boolean) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    if (!shouldAnimate) {
        TaskEntry(task, onCompletedClick)
        return
    }

    AnimatedVisibility(
        visible = visible, enter = fadeIn() + expandVertically()
    ) {
        TaskEntry(task, onCompletedClick)
    }
}