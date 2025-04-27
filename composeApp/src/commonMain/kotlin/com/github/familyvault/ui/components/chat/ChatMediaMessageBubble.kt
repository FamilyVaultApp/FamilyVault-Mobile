package com.github.familyvault.ui.components.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.clip
import com.github.familyvault.models.chat.ChatMessage
import com.github.familyvault.models.enums.chat.ChatMessageAdditionalInfo
import com.github.familyvault.services.IChatService
import com.github.familyvault.ui.components.FullScreenImage
import com.github.familyvault.ui.components.chat.bubbles.ChatMessageBubbleWrapper
import com.github.familyvault.ui.components.typography.Paragraph
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.chat_image_error
import familyvault.composeapp.generated.resources.chat_image_loading
import familyvault.composeapp.generated.resources.chat_message_type_media
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun ChatMediaMessageBubble(
    chatMessage: ChatMessage,
    additionalInfo: ChatMessageAdditionalInfo
) {
    val chatService = koinInject<IChatService>()

    val imageCache = remember { mutableMapOf<String, ImageBitmap?>() }

    var imageBitmap by remember(chatMessage.message) { mutableStateOf<ImageBitmap?>(null) }
    var isLoading by remember(chatMessage.message) { mutableStateOf(true) }
    var isFullScreen by remember { mutableStateOf(false) }

    LaunchedEffect(chatMessage.message) {
        if (imageCache.containsKey(chatMessage.message)) {
            imageBitmap = imageCache[chatMessage.message]
            isLoading = false
        } else {
            isLoading = true
            val bitmap = withContext(Dispatchers.IO) {
                chatService.getImageMediaBitmap(chatMessage.message)
            }
            imageBitmap = bitmap
            imageCache[chatMessage.message] = bitmap
            isLoading = false
        }
    }

    ChatMessageBubbleWrapper(
        message = chatMessage,
        additionalInfo = additionalInfo
    ) {
        if (isLoading) {
            Paragraph(
                text = stringResource(Res.string.chat_image_loading),
                color = AdditionalTheme.colors.mutedColor
            )
        } else if (imageBitmap != null) {
            Image(
                bitmap = imageBitmap!!,
                contentDescription = stringResource(Res.string.chat_message_type_media),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .clickable { isFullScreen = true }
                    .sizeIn(
                        maxWidth = AdditionalTheme.sizing.large,
                        maxHeight = AdditionalTheme.sizing.veryLarge
                    )
                    .clip(RoundedCornerShape(AdditionalTheme.roundness.normalPercent))
            )
        } else {
            Paragraph(
                text = stringResource(Res.string.chat_image_error),
                color = AdditionalTheme.colors.mutedColor
            )
        }
    }

    if (isFullScreen && imageBitmap != null) {
        FullScreenImage(
            imageBitmap = imageBitmap!!,
            onDismiss = { isFullScreen = false }
        )
    }
}