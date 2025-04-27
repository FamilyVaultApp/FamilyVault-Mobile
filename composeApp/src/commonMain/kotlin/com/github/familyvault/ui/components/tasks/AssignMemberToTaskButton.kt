package com.github.familyvault.ui.components.tasks

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.tasks_assign
import org.jetbrains.compose.resources.stringResource

@Composable
fun AssignMemberToTaskButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick
    ) {
        Icon(
            Icons.AutoMirrored.Outlined.ArrowForward, stringResource(Res.string.tasks_assign)
        )
    }
}