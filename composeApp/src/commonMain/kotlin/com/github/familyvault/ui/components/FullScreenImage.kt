package com.github.familyvault.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.window.Dialog
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.chat_media_full_screen
import org.jetbrains.compose.resources.stringResource

@Composable
fun FullScreenImage(
    imageBitmap: ImageBitmap,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
        ) {
            Image(
                bitmap = imageBitmap,
                contentDescription = stringResource(Res.string.chat_media_full_screen),
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.Center),
                contentScale = ContentScale.Fit
            )
        }
    }
}