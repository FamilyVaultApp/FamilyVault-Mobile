package com.github.familyvault.ui.components.chat.bubbles

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import com.github.familyvault.models.chat.ChatMessage
import com.github.familyvault.models.enums.chat.ChatMessageAdditionalInfo
import com.github.familyvault.services.IChatService
import com.github.familyvault.states.IChatImagesState
import com.github.familyvault.ui.components.FullScreenImage
import com.github.familyvault.ui.components.typography.Paragraph
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.chat_image_error
import familyvault.composeapp.generated.resources.chat_image_loading
import familyvault.composeapp.generated.resources.chat_message_type_image
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun ChatImageMessageBubble(
    chatMessage: ChatMessage,
    additionalInfo: ChatMessageAdditionalInfo
) {
    val chatService = koinInject<IChatService>()
    val chatImagesState = koinInject<IChatImagesState>()

    var imageBitmap by remember(chatMessage.id) { mutableStateOf<ImageBitmap?>(null) }
    var isLoading by remember(chatMessage.id) { mutableStateOf(true) }
    var isFullScreen by remember { mutableStateOf(false) }

    LaunchedEffect(chatMessage.message) {
        val imageFromCache = chatImagesState.getImageOrNull(chatMessage.message)

        if (imageFromCache != null) {
            imageBitmap = imageFromCache
            isLoading = false
        } else {
            isLoading = true
            val bitmap = withContext(Dispatchers.IO) {
                chatService.getImageBitmap(chatMessage.message)
            }
            imageBitmap = bitmap
            imageBitmap?.let {
                chatImagesState.storeImage(chatMessage.message, imageBitmap!!)
            }
            isLoading = false
        }
    }

    ChatMessageBubbleWrapper(
        message = chatMessage,
        additionalInfo = additionalInfo,
        additionalPaddings = false
    ) {
        if (isLoading) {
            Paragraph(
                text = stringResource(Res.string.chat_image_loading),
                color = AdditionalTheme.colors.mutedColor
            )
        } else if (imageBitmap != null) {
            Image(
                bitmap = imageBitmap!!,
                contentDescription = stringResource(Res.string.chat_message_type_image),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .clickable { isFullScreen = true }
                    .sizeIn(
                        maxWidth = AdditionalTheme.sizing.large,
                        maxHeight = AdditionalTheme.sizing.veryLarge
                    )
                    .clip(MaterialTheme.shapes.medium)
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