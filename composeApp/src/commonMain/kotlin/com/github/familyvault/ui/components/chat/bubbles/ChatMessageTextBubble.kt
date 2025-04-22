package com.github.familyvault.ui.components.chat.bubbles

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import com.github.familyvault.models.chat.ChatMessage
import com.github.familyvault.models.enums.chat.ChatMessageAdditionalInfo
import com.github.familyvault.ui.theme.AdditionalTheme

@Composable
fun ChatMessageTextBubble(
    message: ChatMessage, additionalInfo: ChatMessageAdditionalInfo
) {
    val messageContent = message.message
    val isAuthor = message.isAuthor

    ChatMessageBubbleWrapper(message, additionalInfo) {
        Text(
            text = messageContent,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = if (isAuthor) TextAlign.End else TextAlign.Start,
            color = if (isAuthor) MaterialTheme.colorScheme.onPrimary else AdditionalTheme.colors.otherChatBubbleContentColor
        )
    }
}