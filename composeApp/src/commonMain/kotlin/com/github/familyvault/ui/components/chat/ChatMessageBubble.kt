package com.github.familyvault.ui.components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.github.familyvault.models.chat.ChatMessage
import com.github.familyvault.ui.components.UserAvatar
import com.github.familyvault.ui.theme.AdditionalTheme

@Composable
fun ChatMessageBubble(
    message: ChatMessage
) {
    val (sender, messageContent, _, _, isAuthor) = message

    Row(
        modifier = Modifier.fillMaxWidth(0.75f),
        horizontalArrangement = if (message.isAuthor) Arrangement.End else Arrangement.Start,
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
                text = messageContent,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = if (isAuthor) TextAlign.End else TextAlign.Start,
                color = if (isAuthor) MaterialTheme.colorScheme.onPrimary else Color.Black
            )
        }
    }
}