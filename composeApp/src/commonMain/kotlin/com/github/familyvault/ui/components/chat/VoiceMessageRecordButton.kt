package com.github.familyvault.ui.components.chat

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.chat_start_recording_description
import org.jetbrains.compose.resources.stringResource

@Composable
fun VoiceMessageRecordButton(onStart: () -> Unit) {
    IconButton(onClick = onStart) {
        Icon(
            imageVector = Icons.Outlined.Mic,
            contentDescription = stringResource(Res.string.chat_start_recording_description),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}