package com.github.familyvault.ui.components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.github.familyvault.models.chat.ChatThread
import com.github.familyvault.models.enums.chat.ChatMessageContentType
import com.github.familyvault.services.IFamilyGroupSessionService
import com.github.familyvault.ui.components.UserAvatar
import com.github.familyvault.ui.components.typography.Headline3
import com.github.familyvault.ui.components.typography.ParagraphMuted
import com.github.familyvault.ui.theme.AdditionalTheme
import com.github.familyvault.utils.TextShortener
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.chat_message_type_audio
import familyvault.composeapp.generated.resources.chat_message_type_image
import familyvault.composeapp.generated.resources.you
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun ChatThreadEntry(
    chatThread: ChatThread, unreadMessages: Boolean, onClick: () -> Unit = {}
) {
    val familyGroupSessionService = koinInject<IFamilyGroupSessionService>()
    val backgroundColor =
        if (unreadMessages) MaterialTheme.colorScheme.primaryContainer else Color.Unspecified
    val lastMessage = chatThread.lastMessage
    val currentUser = remember { familyGroupSessionService.getCurrentUser() }

    val senderName = if (lastMessage?.senderId == currentUser.id) {
        stringResource(Res.string.you)
    } else {
        lastMessage?.senderId
    }

    return Row(
        modifier = Modifier.defaultMinSize(minHeight = AdditionalTheme.sizing.entryMinSize)
            .fillMaxWidth()
            .clickable(onClick = onClick).background(backgroundColor)
            .padding(
                horizontal = AdditionalTheme.spacings.screenPadding,
                vertical = AdditionalTheme.spacings.normalPadding,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.medium)
    ) {
        UserAvatar(chatThread.name)
        Column {
            Headline3(
                TextShortener.shortenText(chatThread.name, 30)
            )
            if (lastMessage != null) {
                when(lastMessage.type) {
                    ChatMessageContentType.VOICE -> ParagraphMuted(TextShortener.shortenText("${senderName}: ${stringResource(Res.string.chat_message_type_audio)}", 30))
                    ChatMessageContentType.IMAGE -> ParagraphMuted(TextShortener.shortenText("${senderName}: ${stringResource(Res.string.chat_message_type_image)}", 30))
                    ChatMessageContentType.TEXT -> ParagraphMuted(TextShortener.shortenText("${senderName}: ${lastMessage.message}", 30))
                }
            }
        }
    }
}