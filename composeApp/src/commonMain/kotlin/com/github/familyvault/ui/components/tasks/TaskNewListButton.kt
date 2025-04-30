package com.github.familyvault.ui.components.tasks

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.task_add_new_list_short
import org.jetbrains.compose.resources.stringResource

@Composable
fun TaskNewListButton(onClick: () -> Unit) {
    TaskListButton(
        stringResource(Res.string.task_add_new_list_short),
        Icons.Filled.Add,
        onClick = onClick
    )
}