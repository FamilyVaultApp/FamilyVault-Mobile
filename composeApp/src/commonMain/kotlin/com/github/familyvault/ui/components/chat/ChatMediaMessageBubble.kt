package com.github.familyvault.ui.components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import com.github.familyvault.models.chat.ChatMessage
import com.github.familyvault.services.IChatService
import com.github.familyvault.services.IMediaPickerService
import com.github.familyvault.ui.components.UserAvatar
import com.github.familyvault.ui.components.typography.Paragraph
import com.github.familyvault.ui.theme.AdditionalTheme
import org.koin.compose.koinInject
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.chat_image_description
import org.jetbrains.compose.resources.stringResource


@Composable
fun ChatMediaMessageBubble(
    message: ChatMessage
) {
    val isAuthor = message.isAuthor
    val sender = message.senderId

    val chatService = koinInject<IChatService>()
    val mediaPickerService = koinInject<IMediaPickerService>()
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(message.message) {
        val bytes = chatService.getMediaMessage(message.message)
        imageBitmap = mediaPickerService.getBitmapFromBytes(bytes)
    }

    Row(
        modifier = Modifier.fillMaxWidth(0.75f),
        horizontalArrangement = if (isAuthor) Arrangement.End else Arrangement.Start,
    ) {
        if (!isAuthor) {
            UserAvatar(firstName = sender, size = AdditionalTheme.spacings.large)
            Spacer(modifier = Modifier.width(AdditionalTheme.spacings.medium))
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.small),
        ) {
            if (!isAuthor) {
                Paragraph(sender, color = AdditionalTheme.colors.mutedColor)
            }

            Row(
                modifier = Modifier
                    .background(
                        if (isAuthor) MaterialTheme.colorScheme.primary else AdditionalTheme.colors.otherChatBubbleColor,
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(AdditionalTheme.spacings.medium),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                imageBitmap?.let {
                    Image(
                        bitmap = it,
                        contentDescription = stringResource(Res.string.chat_image_description),
                        modifier = Modifier.size(AdditionalTheme.sizing.large)
                    )
                }
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
