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
import familyvault.composeapp.generated.resources.chat_add_multimedia_description
import familyvault.composeapp.generated.resources.chat_cancel_recording_description
import familyvault.composeapp.generated.resources.chat_message_send_description
import familyvault.composeapp.generated.resources.chat_send_message
import familyvault.composeapp.generated.resources.chat_start_recording_description
import familyvault.composeapp.generated.resources.user_modification_description
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

        if (isRecording) {
            VoiceMessageStopButton(
                onCancel = {
                    audioRecorder.stop()
                    isRecording = false
                    audioData = ByteArray(0)
                }
            )
        } else {
            MultimediaSendButton(onClick = { /* Obsługa zdjęć */ })
            VoiceMessageRecordButton(
                onStart = {
                    if (!audioRecorder.haveRecordingPermission()) {
                        audioRecorder.requestRecordingPermission()
                    } else {
                        audioRecorder.start()
                        isRecording = true
                    }
                }
            )
        }

        Spacer(modifier = Modifier.width(AdditionalTheme.spacings.small))

        AnimatedContent(
            targetState = isRecording,
            modifier = Modifier.weight(1f)
        ) { recording ->
            if (recording) {
                VoiceMessageRecordingWaves(
                    modifier = Modifier
                        .height(AdditionalTheme.spacings.veryLarge)
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = MaterialTheme.shapes.large
                        )
                        .padding(horizontal = AdditionalTheme.spacings.normalPadding),
                )
            } else {
                TextField(
                    label = stringResource(Res.string.chat_send_message),
                    modifier = Modifier
                        .height(AdditionalTheme.spacings.veryLarge)
                        .fillMaxWidth(),
                    value = textMessage,
                    onValueChange = { textMessage = it },
                )
            }
        }

        SendButton(
            isRecording = isRecording,
            textMessage = textMessage,
            onSendText = {
                onTextMessageSend(textMessage)
                textMessage = ""
            },
            onSendVoice = {
                audioData = audioRecorder.stop()
                onVoiceMessageSend(audioData)
                isRecording = false
            }
        )
    }
}
