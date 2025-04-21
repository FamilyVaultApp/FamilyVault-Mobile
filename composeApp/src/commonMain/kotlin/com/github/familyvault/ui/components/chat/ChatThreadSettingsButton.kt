package com.github.familyvault.ui.components.chat

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.chat_settings
import org.jetbrains.compose.resources.stringResource

@Composable
fun ChatThreadSettingsButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick
    ) {
        Icon(
            Icons.Filled.Settings,
            stringResource(Res.string.chat_settings)
        )
    }
}