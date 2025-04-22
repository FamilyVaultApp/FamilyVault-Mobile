package com.github.familyvault.ui.components.chat.bubbles

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.github.familyvault.models.chat.ChatMessage
import com.github.familyvault.models.enums.chat.ChatMessageAdditionalInfo
import com.github.familyvault.services.IAudioPlayerService
import com.github.familyvault.services.IChatService
import com.github.familyvault.ui.components.chat.ChatAudioPlayerWaveform
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.chat_recording_play_description
import familyvault.composeapp.generated.resources.chat_recording_stop_description
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun ChatVoiceMessageBubble(
    message: ChatMessage,
    additionalInfo: ChatMessageAdditionalInfo
) {
    val audioPlayerService = koinInject<IAudioPlayerService>()
    val chatService = koinInject<IChatService>()
    val coroutineScope = rememberCoroutineScope()

    val isAuthor = message.isAuthor

    var isPlaying by remember { mutableStateOf(false) }

    ChatMessageBubbleWrapper(message, additionalInfo) {
        IconButton(
            onClick = {
                coroutineScope.launch {
                    if (isPlaying) {
                        audioPlayerService.stop()
                        isPlaying = false
                    } else {
                        val audio = chatService.getVoiceMessage(message.message)
                        audioPlayerService.play(audio) {
                            isPlaying = false
                        }
                        isPlaying = true
                    }
                }
            }
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                contentDescription = stringResource(
                    if (isPlaying)
                        Res.string.chat_recording_stop_description
                    else
                        Res.string.chat_recording_play_description
                ),
                tint = if (isAuthor) MaterialTheme.colorScheme.onPrimary else AdditionalTheme.colors.otherChatBubbleContentColor
            )
        }

        ChatAudioPlayerWaveform(
            isPlaying = isPlaying,
            isAuthor = isAuthor
        )
    }
}