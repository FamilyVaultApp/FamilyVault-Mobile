package com.github.familyvault.ui.components.chat.bubbles

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.familyvault.models.chat.ChatMessage
import com.github.familyvault.models.enums.chat.ChatMessageAdditionalInfo
import com.github.familyvault.models.enums.chat.shouldRenderMessageSendDate
import com.github.familyvault.models.enums.chat.shouldRenderSenderName
import com.github.familyvault.ui.components.UserAvatar
import com.github.familyvault.ui.components.typography.Paragraph
import com.github.familyvault.ui.theme.AdditionalTheme
import com.github.familyvault.utils.TimeFormatter

@Composable
fun ChatMessageBubbleWrapper(
    message: ChatMessage,
    additionalInfo: ChatMessageAdditionalInfo,
    additionalPaddings: Boolean = true,
    content: @Composable () -> Unit
) {
    val sender = message.senderId
    val isAuthor = message.isAuthor

    Row(
        modifier = Modifier.fillMaxWidth(0.75f),
        horizontalArrangement = if (message.isAuthor) Arrangement.End else Arrangement.Start,
    ) {
        if (!isAuthor && additionalInfo.shouldRenderSenderName()) {
            Box(modifier = Modifier.padding(top = AdditionalTheme.spacings.medium)) {
                UserAvatar(firstName = sender.firstname, size = AdditionalTheme.spacings.large)
            }
        } else {
            Spacer(modifier = Modifier.width(AdditionalTheme.spacings.large))
        }
        Column(
            modifier = Modifier.padding(start = AdditionalTheme.spacings.medium),
            verticalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.small),
            horizontalAlignment = if (message.isAuthor) Alignment.End else Alignment.Start
        ) {
            if (!isAuthor && additionalInfo.shouldRenderSenderName()) {
                Paragraph(message.senderId.fullname, color = AdditionalTheme.colors.mutedColor)
            }
            Row(
                modifier = Modifier
                    .background(
                        if (isAuthor) MaterialTheme.colorScheme.primary else AdditionalTheme.colors.otherChatBubbleColor,
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(if (additionalPaddings) AdditionalTheme.spacings.medium else 0.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                content()
            }
            if (additionalInfo.shouldRenderMessageSendDate()) {
                Paragraph(
                    TimeFormatter.formatTime(message.sendDate),
                    color = AdditionalTheme.colors.mutedColor
                )
            }
        }
    }
}