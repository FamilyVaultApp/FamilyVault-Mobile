package com.github.familyvault.ui.components.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.github.familyvault.models.chat.ChatMessage
import com.github.familyvault.models.enums.ChatMessageContentType
import com.github.familyvault.ui.theme.AdditionalTheme

@Composable
fun ChatMessageEntry(
    message: ChatMessage
) {
    val isAuthor = message.isAuthor

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isAuthor) Alignment.End else Alignment.Start
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AdditionalTheme.spacings.small),
            horizontalArrangement = if (isAuthor) Arrangement.End else Arrangement.Start,
        ) {
            when (message.type) {
                ChatMessageContentType.TEXT -> ChatMessageTextBubble(message)
                ChatMessageContentType.VOICE -> ChatVoiceMessageBubble(message)
                ChatMessageContentType.IMAGE -> null
            }
        }
    }
}