package com.github.familyvault.ui.components.chat

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.chat_message_send_description
import org.jetbrains.compose.resources.stringResource

@Composable
fun SendButton(
    isRecording: Boolean,
    textMessage: String,
    selectedMediaUrl: List<String>,
    onSendText: () -> Unit,
    onSendVoice: () -> Unit,
    onSendMedia: () -> Unit
) {
    val enabled = isRecording || textMessage.isNotBlank() || selectedMediaUrl.isNotEmpty()

    IconButton(
        onClick = {
            when {
                isRecording -> onSendVoice()
                textMessage.isNotBlank() -> onSendText()
                selectedMediaUrl.isNotEmpty() -> onSendMedia()
            }
        },
        enabled = enabled
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Send,
            contentDescription = stringResource(Res.string.chat_message_send_description),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}
