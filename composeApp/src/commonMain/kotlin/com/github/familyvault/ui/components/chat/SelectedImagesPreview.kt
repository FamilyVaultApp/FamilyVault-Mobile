package com.github.familyvault.ui.components.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.chat_image_preview
import familyvault.composeapp.generated.resources.chat_remove_multimedia_description
import org.jetbrains.compose.resources.stringResource

@Composable
fun SelectedImagesPreview(
    selectedImages: List<String>,
    onRemoveImage: (String) -> Unit,
    getBytesFromUri: (String) -> ByteArray?,
    getBitmapFromBytes: (ByteArray) -> androidx.compose.ui.graphics.ImageBitmap
) {
    if (selectedImages.isNotEmpty()) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = AdditionalTheme.spacings.medium,
                    vertical = AdditionalTheme.spacings.small
                ),
            horizontalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.medium)
        ) {
            items(selectedImages) { uriString ->
                val imageBytes = getBytesFromUri(uriString)
                val bitmap = imageBytes?.let { getBitmapFromBytes(it) }

                bitmap?.let {
                    Box(
                        modifier = Modifier
                            .size(AdditionalTheme.sizing.medium)
                            .clip(RoundedCornerShape(AdditionalTheme.roundness.normalPercent))
                    ) {
                        Image(
                            bitmap = it,
                            contentDescription = stringResource(Res.string.chat_image_preview),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(Res.string.chat_remove_multimedia_description),
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(AdditionalTheme.spacings.small)
                                .size(AdditionalTheme.sizing.small)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .clickable {
                                    onRemoveImage(uriString)
                                }
                        )
                    }
                }
            }
        }
    }
}