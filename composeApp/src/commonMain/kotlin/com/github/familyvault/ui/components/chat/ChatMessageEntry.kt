package com.github.familyvault.ui.components.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.familyvault.models.chat.ChatMessage
import com.github.familyvault.ui.theme.AdditionalTheme

@Composable
fun ChatMessageEntry(
    message: ChatMessage
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(AdditionalTheme.spacings.small),
        horizontalArrangement = if (message.isAuthor) Arrangement.End else Arrangement.Start,
    ) {
        ChatMessageBubble(message)
    }
}