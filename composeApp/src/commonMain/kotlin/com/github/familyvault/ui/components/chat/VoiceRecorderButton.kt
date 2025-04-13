package com.github.familyvault.ui.components.chat

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.github.familyvault.services.ChatService
import com.github.familyvault.services.IAudioRecorderService

@Composable
fun VoiceRecorderButton(
    audioRecorder: IAudioRecorderService,
) {
    var isRecording by remember { mutableStateOf(false) }

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

                val audioData: ByteArray = audioRecorder.getAudioBytes()
                println(audioData)
            }
        }
    }) {
        Icon(
            imageVector = if (isRecording) Icons.Default.Stop else Icons.Outlined.Mic,
            contentDescription = if (isRecording) "Stop recording" else "Start recording"
        )
    }
}