package com.github.familyvault.ui.components.chat

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.github.familyvault.services.IAudioRecorderService
import com.github.familyvault.services.IImagePickerService
import com.github.familyvault.ui.components.overrides.TextField
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.chat_send_message
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun ChatInputField(
    onTextMessageSend: (message: String) -> Unit,
    onVoiceMessageSend: (audio: ByteArray) -> Unit,
    onImageMessageSend: (media: List<ByteArray>) -> Unit
) {
    val audioRecorder = koinInject<IAudioRecorderService>()
    val imagePicker = koinInject<IImagePickerService>()

    var textMessage by remember { mutableStateOf("") }
    var isRecording by remember { mutableStateOf(false) }
    var audioData by remember { mutableStateOf(ByteArray(0)) }

    SelectedImagesPreview(
        selectedImages = imagePicker.getSelectedImageUrls(),
        onRemoveImage = { uri ->
            imagePicker.removeSelectedImage(uri)
        },
        getBytesFromUri = { uri -> imagePicker.getBytesFromUri(uri) },
        getBitmapFromBytes = { bytes -> imagePicker.getBitmapFromBytes(bytes) }
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                AdditionalTheme.spacings.small,
                bottom = AdditionalTheme.spacings.large
            ),
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
            MultimediaPickerButton(
                onClick = {
                    imagePicker.openMediaPickerForSelectingImages()
                }
            )
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
            selectedImageUrls = imagePicker.getSelectedImageUrls(),
            onSendText = {
                onTextMessageSend(textMessage)
                textMessage = ""
            },
            onSendVoice = {
                audioData = audioRecorder.stop()
                onVoiceMessageSend(audioData)
                isRecording = false
            },
            onSendImage = {
                val mediaByteArrays = imagePicker.getSelectedImageAsByteArrays()
                imagePicker.clearSelectedImages()
                onImageMessageSend(mediaByteArrays)
            }
        )
    }
}
