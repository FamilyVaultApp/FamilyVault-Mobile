package com.github.familyvault.ui.components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.github.familyvault.models.chat.ChatMessage
import com.github.familyvault.ui.components.UserAvatar
import com.github.familyvault.ui.theme.AdditionalTheme
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import com.github.familyvault.services.IAudioPlayerService
import com.github.familyvault.services.IChatService

@Composable
fun ChatMessageRecordingBubble(
    message: ChatMessage
) {
    val audioPlayerService = koinInject<IAudioPlayerService>()
    val chatService = koinInject<IChatService>()
    val coroutineScope = rememberCoroutineScope()

    val sender = message.senderId
    val isAuthor = message.isAuthor

    var isPlaying by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(0.75f),
        horizontalArrangement = if (isAuthor) Arrangement.End else Arrangement.Start,
    ) {
        if (!isAuthor) {
            UserAvatar(firstName = sender, size = AdditionalTheme.spacings.large)
        }

        Row(
            modifier = Modifier
                .padding(AdditionalTheme.spacings.medium)
                .background(
                    if (isAuthor) MaterialTheme.colorScheme.primary else AdditionalTheme.colors.otherChatBubbleColor,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(AdditionalTheme.spacings.medium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        if (isPlaying) {
                            audioPlayerService.stop()
                            isPlaying = false
                        } else {
                            val audio = chatService.getVoiceMessage(message.message)
                            isPlaying = true
                            audioPlayerService.play(audio) {
                                isPlaying = false
                            }
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = if (isAuthor) MaterialTheme.colorScheme.onPrimary else Color.Black
                )
            }

            ChatAudioPlayerWaveform(
                isPlaying = isPlaying,
                isAuthor = isAuthor
            )
        }
    }
}