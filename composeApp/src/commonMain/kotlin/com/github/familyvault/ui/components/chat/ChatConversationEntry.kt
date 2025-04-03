package com.github.familyvault.ui.components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.github.familyvault.models.chat.ChatMessage
import com.github.familyvault.ui.components.UserAvatar
import com.github.familyvault.ui.components.typography.Headline3
import com.github.familyvault.ui.components.typography.ParagraphMuted
import com.github.familyvault.ui.theme.AdditionalTheme

@Composable
fun ChatConversationEntry(
    title: String, lastMessage: ChatMessage, unreadMessages: Boolean, onClick: () -> Unit = {}
) {
    val backgroundColor =
        if (unreadMessages) MaterialTheme.colorScheme.primaryContainer else Color.Unspecified

    return Row(
        modifier = Modifier.fillMaxWidth()
            .clickable(onClick = onClick).background(backgroundColor).padding(
                horizontal = AdditionalTheme.spacings.screenPadding,
                vertical = AdditionalTheme.spacings.normalPadding,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.medium)
    ) {
        UserAvatar(title)
        Column {
            Headline3(title)
            ParagraphMuted("${lastMessage.sender}: ${lastMessage.messageShortPreview}")
        }
    }
}