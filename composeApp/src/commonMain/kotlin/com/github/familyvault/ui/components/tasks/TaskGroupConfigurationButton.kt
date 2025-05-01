package com.github.familyvault.ui.components.tasks

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.task_settings
import org.jetbrains.compose.resources.stringResource

@Composable
fun TaskGroupConfigurationButton(onClick: () -> Unit) {
    IconButton(
        modifier = Modifier.size(AdditionalTheme.sizing.userAvatarSize),
        onClick = onClick
    ) {
        Icon(Icons.Filled.MoreVert, stringResource(Res.string.task_settings))
    }
}