package com.github.familyvault.ui.components.chat

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.familyvault.services.IAudioRecorderService
import com.github.familyvault.ui.components.overrides.TextField
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
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
    var audioData by remember { mutableStateOf(ByteArray(0)) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(AdditionalTheme.spacings.small, vertical = AdditionalTheme.spacings.large),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            if (isRecording) {
                audioRecorder.stop()
                isRecording = false
                audioData = ByteArray(0)
            } else {
                // Obsługa zdjęć
            }
        }) {
            Icon(
                imageVector = if (isRecording) Icons.Default.Delete else Icons.Outlined.Image,
                contentDescription = null,
                tint = if (isRecording) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (!isRecording) {
            IconButton(onClick = {
                if (!audioRecorder.haveRecordingPermission()) {
                    audioRecorder.requestRecordingPermission()
                } else {
                    audioRecorder.start()
                    isRecording = true
                }
            }) {
                Icon(
                    imageVector = Icons.Outlined.Mic,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.width(4.dp))

        AnimatedContent(
            targetState = isRecording,
            modifier = Modifier
                .weight(1f)
        ) { recording ->
            Box(
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                if (recording) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                shape = MaterialTheme.shapes.large
                            )
                            .padding(horizontal = 12.dp)
                    ) {
                        VoiceMessageRecordingWaves(
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                } else {
                    TextField(
                        label = stringResource(Res.string.chat_send_message),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent),
                        value = textMessage,
                        onValueChange = { textMessage = it },
                    )
                }
            }
        }

        IconButton(
            onClick = {
                when {
                    isRecording -> {
                        audioRecorder.stop()
                        audioData = audioRecorder.getAudioBytes()
                        onVoiceMessageSend(audioData)
                        isRecording = false
                    }

                    textMessage.isNotBlank() -> {
                        onTextMessageSend(textMessage)
                        textMessage = ""
                    }
                }
            },
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}