package com.github.familyvault.ui.components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.github.familyvault.models.chat.ChatMessage
import com.github.familyvault.ui.components.UserAvatar
import com.github.familyvault.ui.components.typography.Paragraph
import com.github.familyvault.ui.theme.AdditionalTheme

@Composable
fun ChatMessageTextBubble(
    message: ChatMessage
) {
    val sender = message.senderId
    val messageContent = message.message
    val isAuthor = message.isAuthor

    Row(
        modifier = Modifier.fillMaxWidth(0.75f),
        horizontalArrangement = if (message.isAuthor) Arrangement.End else Arrangement.Start,
    ) {
        if (!isAuthor) {
            UserAvatar(firstName = sender, size = AdditionalTheme.spacings.large)
        }
        Column(
            modifier = Modifier.padding(start = AdditionalTheme.spacings.medium),
            verticalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.small)
        ) {
            if (!isAuthor) {
                Paragraph(message.senderId, color = AdditionalTheme.colors.mutedColor)
            }
            Box(
                modifier = Modifier
                    .background(
                        if (isAuthor) MaterialTheme.colorScheme.primary else AdditionalTheme.colors.otherChatBubbleColor,
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(AdditionalTheme.spacings.medium),
            ) {
                Text(
                    text = messageContent,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = if (isAuthor) TextAlign.End else TextAlign.Start,
                    color = if (isAuthor) MaterialTheme.colorScheme.onPrimary else AdditionalTheme.colors.otherChatBubbleContentColor
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