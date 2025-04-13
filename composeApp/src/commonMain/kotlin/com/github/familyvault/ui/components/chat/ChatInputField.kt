package com.github.familyvault.ui.components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.github.familyvault.services.IAudioRecorderService
import com.github.familyvault.services.IChatService
import com.github.familyvault.states.ICurrentChatState
import com.github.familyvault.ui.components.overrides.TextField
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.chat_attach_photo
import familyvault.composeapp.generated.resources.chat_recording_icon
import familyvault.composeapp.generated.resources.chat_send_message
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun ChatInputField(
    onTextMessageSend: (message: String) -> Unit,
    onVoiceMessageSend: (audio: ByteArray) -> Unit
) {
    val audioRecorder = koinInject<IAudioRecorderService>()

    var textMessage by remember { mutableStateOf("") }
    var isRecording by remember { mutableStateOf(false) }
    var audioData = audioRecorder.getAudioBytes()

    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(AdditionalTheme.spacings.small, vertical = AdditionalTheme.spacings.large),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /* Dodaj załącznik */ }) {
            Icon(
                Icons.Outlined.Image,
                contentDescription = stringResource(Res.string.chat_attach_photo),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        IconButton(onClick = {
            if (!audioRecorder.checkRecordingPermission()) {
                audioRecorder.requestRecordingPermission()
            } else {
                if (!isRecording) {
                    audioRecorder.start()
                    isRecording = true
                } else {
                    audioRecorder.stop()
                    isRecording = false

                    audioData = audioRecorder.getAudioBytes()
                }
            }
        }) {
            Icon(
                imageVector = if (isRecording) Icons.Default.Stop else Icons.Outlined.Mic,
                contentDescription = if (isRecording) "Stop recording" else "Start recording"
            )
        }

        TextField(
            label = stringResource(Res.string.chat_send_message),
            modifier = Modifier.weight(1f).background(Color.Transparent),
            value = textMessage,
            onValueChange = { textMessage = it },
        )

        IconButton(onClick = {
            onTextMessageSend(textMessage)
            onVoiceMessageSend(audioData)
            textMessage = ""
        }) {
            Icon(
                Icons.AutoMirrored.Filled.Send,
                contentDescription = stringResource(Res.string.chat_send_message),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}