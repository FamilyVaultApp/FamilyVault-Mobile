package com.github.familyvault.ui.components.tasks

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.tasks_settings
import org.jetbrains.compose.resources.stringResource

@Composable
fun TaskGroupConfigurationButton(onClick: () -> Unit) {
    IconButton(
        onClick
    ) {
        Icon(Icons.Filled.MoreVert, stringResource(Res.string.tasks_settings))
    }
}