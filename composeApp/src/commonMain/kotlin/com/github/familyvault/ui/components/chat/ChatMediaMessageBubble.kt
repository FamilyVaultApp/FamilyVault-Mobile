package com.github.familyvault.ui.components.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import com.github.familyvault.models.chat.ChatMessage
import com.github.familyvault.services.IChatService
import com.github.familyvault.services.IMediaPickerService
import com.github.familyvault.ui.components.UserAvatar
import com.github.familyvault.ui.components.typography.Paragraph
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.chat_media_description
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun ChatMediaMessageBubble(
    message: ChatMessage
) {
    val isAuthor = message.isAuthor
    val sender = message.senderId

    val chatService = koinInject<IChatService>()
    val mediaPickerService = koinInject<IMediaPickerService>()
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var isFullScreen by remember { mutableStateOf(false) }

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
            horizontalAlignment = if (isAuthor) Alignment.End else Alignment.Start
        ) {
            if (!isAuthor) {
                Paragraph(
                    text = sender,
                    color = AdditionalTheme.colors.mutedColor
                )
            }

            imageBitmap?.let { bitmap ->
                Image(
                    bitmap = bitmap,
                    contentDescription = stringResource(Res.string.chat_media_description),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .clickable { isFullScreen = true }
                        .sizeIn(
                            maxWidth = AdditionalTheme.sizing.large,
                            maxHeight = AdditionalTheme.sizing.veryLarge
                        )
                        .clip(RoundedCornerShape(AdditionalTheme.roundness.normalPercent))
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

    if (isFullScreen && imageBitmap != null) {
        FullScreenImage(
            imageBitmap = imageBitmap!!,
            onDismiss = { isFullScreen = false }
        )
    }
}
