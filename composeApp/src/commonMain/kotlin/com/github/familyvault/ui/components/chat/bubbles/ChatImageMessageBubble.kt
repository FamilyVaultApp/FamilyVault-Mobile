package com.github.familyvault.ui.components.chat.bubbles

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.github.familyvault.models.ImageSize
import com.github.familyvault.models.chat.ChatImageMessageMetadata
import com.github.familyvault.models.chat.ChatMessage
import com.github.familyvault.models.enums.chat.ChatMessageAdditionalInfo
import com.github.familyvault.services.IChatService
import com.github.familyvault.states.IChatImagesState
import com.github.familyvault.ui.components.FullScreenImage
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.chat_message_type_image
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun ChatImageMessageBubble(
    chatMessage: ChatMessage, additionalInfo: ChatMessageAdditionalInfo
) {
    val chatService = koinInject<IChatService>()
    val chatImagesState = koinInject<IChatImagesState>()

    var chatImageMessageMetadata by remember { mutableStateOf<ChatImageMessageMetadata?>(null) }
    var imageSize by remember { mutableStateOf(ImageSize.Unspecified) }
    var imageBitmap by remember(chatMessage.id) { mutableStateOf<ImageBitmap?>(null) }
    var isFullScreen by remember { mutableStateOf(false) }

    LaunchedEffect(chatMessage.message) {
        chatImageMessageMetadata =
            Json.decodeFromString<ChatImageMessageMetadata>(chatMessage.message)
        val imageMetadata = requireNotNull(chatImageMessageMetadata)
        val imageFromCache = chatImagesState.getImageOrNull(imageMetadata.storeFileId)
        imageSize = ImageSize(imageMetadata.height, imageMetadata.width)

        if (imageFromCache != null) {
            imageBitmap = imageFromCache
        } else {
            val bitmap = withContext(Dispatchers.IO) {
                chatService.getImageBitmap(imageMetadata.storeFileId)
            }
            imageBitmap = bitmap
            imageBitmap?.let {
                chatImagesState.storeImage(imageMetadata.storeFileId, imageBitmap!!)
            }
        }
    }

    ChatMessageBubbleWrapper(
        message = chatMessage,
        additionalInfo = additionalInfo,
        additionalPaddings = false,
    ) {
        Column(
            modifier = Modifier.aspectRatio(imageSize.aspectRatio)
                .size(imageSize.width.dp, imageSize.height.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (imageBitmap != null) {
                Image(
                    bitmap = requireNotNull(imageBitmap),
                    contentDescription = stringResource(Res.string.chat_message_type_image),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.clickable { isFullScreen = true }.fillMaxSize()
                        .clip(MaterialTheme.shapes.medium)
                )
            }
        }
    }

    if (isFullScreen && imageBitmap != null) {
        FullScreenImage(
            imageBitmap = imageBitmap!!, onDismiss = { isFullScreen = false })
    }
}
