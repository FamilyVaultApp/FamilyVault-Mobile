package com.github.familyvault.ui.components.chat.messageEntry

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.github.familyvault.models.chat.ChatMessage
import com.github.familyvault.models.enums.chat.ChatMessageContentType
import com.github.familyvault.ui.components.chat.ChatMediaMessageBubble
import com.github.familyvault.ui.components.chat.bubbles.ChatMessageTextBubble
import com.github.familyvault.ui.components.chat.bubbles.ChatVoiceMessageBubble
import com.github.familyvault.ui.theme.AdditionalTheme
import com.github.familyvault.utils.ChatMessageAdditionalInfoClassifier

@Composable
fun ChatMessageEntry(
    message: ChatMessage, prevMessage: ChatMessage?, nextMessage: ChatMessage?
) {
    val isAuthor = message.isAuthor

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isAuthor) Alignment.End else Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(AdditionalTheme.spacings.small / 2),
            horizontalArrangement = if (isAuthor) Arrangement.End else Arrangement.Start,
        ) {
            val additionalInfo =
                ChatMessageAdditionalInfoClassifier.classify(message, prevMessage, nextMessage)

            when (message.type) {
                ChatMessageContentType.TEXT -> ChatMessageTextBubble(
                    message, additionalInfo
                )

                ChatMessageContentType.VOICE -> ChatVoiceMessageBubble(
                    message, additionalInfo
                )

                ChatMessageContentType.MEDIA -> ChatMediaMessageBubble(message)
            }
        }
    }
}