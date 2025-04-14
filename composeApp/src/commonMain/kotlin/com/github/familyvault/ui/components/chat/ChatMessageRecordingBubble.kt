package com.github.familyvault.ui.components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
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
    val audio = chatService.getVoiceMessage(message.message)

    Row(
        modifier = Modifier.fillMaxWidth(0.75f),
        horizontalArrangement = if (isAuthor) Arrangement.End else Arrangement.Start,
    ) {
        if (!isAuthor) {
            UserAvatar(firstName = sender, size = AdditionalTheme.spacings.large)
        }

        Column(
            modifier = Modifier
                .padding(AdditionalTheme.spacings.small)
                .background(
                    if (isAuthor) MaterialTheme.colorScheme.primary else AdditionalTheme.colors.otherChatBubbleColor,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(AdditionalTheme.spacings.medium),
            horizontalAlignment = if (isAuthor) Alignment.End else Alignment.Start
        ) {
            Text(
                text = "🎙️ Wiadomość głosowa",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = if (isAuthor) TextAlign.End else TextAlign.Start,
                color = if (isAuthor) MaterialTheme.colorScheme.onPrimary else Color.Black
            )
            Spacer(modifier = Modifier.height(AdditionalTheme.spacings.small))
            Button(onClick = {
                coroutineScope.launch {
                    audioPlayerService.play(audio)
                }
            }) {
                Text("▶️ Odtwórz")
            }
        }
    }
}