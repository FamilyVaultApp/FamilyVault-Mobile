package com.github.familyvault.ui.components.chat

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.chat_cancel_recording_description
import org.jetbrains.compose.resources.stringResource

@Composable
fun VoiceMessageStopButton(onCancel: () -> Unit) {
    IconButton(onClick = onCancel) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = stringResource(Res.string.chat_cancel_recording_description),
            tint = MaterialTheme.colorScheme.error
        )
    }
}