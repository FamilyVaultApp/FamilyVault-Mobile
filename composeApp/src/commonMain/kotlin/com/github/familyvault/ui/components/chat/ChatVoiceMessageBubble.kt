package com.github.familyvault.ui.components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.github.familyvault.models.chat.ChatMessage
import com.github.familyvault.services.IAudioPlayerService
import com.github.familyvault.services.IChatService
import com.github.familyvault.ui.components.UserAvatar
import com.github.familyvault.ui.components.typography.Paragraph
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.chat_recording_play_description
import familyvault.composeapp.generated.resources.chat_recording_stop_description
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun ChatVoiceMessageBubble(
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
            Spacer(modifier = Modifier.width(AdditionalTheme.spacings.medium))
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.small),
        ) {
            if (!isAuthor) {
                Paragraph(sender, color = AdditionalTheme.colors.mutedColor)
            }
            Row(
                modifier = Modifier
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
            if (!isAuthor) {
                Paragraph(
                    "${message.sendDate.hour}:${message.sendDate.minute}",
                    color = AdditionalTheme.colors.mutedColor
                )
            }
        }
    }
}