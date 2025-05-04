package com.github.familyvault.ui.screens.main.filesCabinet

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import com.github.familyvault.services.IFileCabinetService
import com.github.familyvault.services.IImagePickerService
import com.github.familyvault.ui.components.FullScreenImage
import com.github.familyvault.ui.components.LoaderWithText
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.file_cabinet_photo_card
import familyvault.composeapp.generated.resources.loading
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun PhotosTabContent() {
    val fileCabinetService = koinInject<IFileCabinetService>()
    val imagePicker = koinInject<IImagePickerService>()
    val storeId = fileCabinetService.retrieveFileCabinetStoreId()

    var imageByteArrays by remember { mutableStateOf<List<ByteArray>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var fullScreenImage by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(Unit) {
        isLoading = true
        imageByteArrays = withContext(Dispatchers.IO) {
            fileCabinetService.getImagesFromFamilyGroupStoreAsByteArray(
                storeId = storeId,
                limit = 30,
                skip = 0
            ).filterNotNull()
        }
        isLoading = false
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            LoaderWithText(
                stringResource(Res.string.loading), modifier = Modifier.fillMaxSize()
            )
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(AdditionalTheme.spacings.small)
        ) {
            items(imageByteArrays.size) { index ->
                val imageBytes = imageByteArrays[index]

                val imageBitmapState = produceState<ImageBitmap?>(initialValue = null, imageBytes) {
                    withContext(Dispatchers.IO) {
                        value = imagePicker.getBitmapFromBytes(imageBytes)
                    }
                }

                val bitmap = imageBitmapState.value
                if (bitmap == null) {
                    LoadingCard()
                } else {
                    PhotoCard(
                        imageBitmap = bitmap,
                        onClick = { fullScreenImage = bitmap }
                    )
                }
            }
        }
    }

    if (fullScreenImage != null) {
        FullScreenImage(
            imageBitmap = fullScreenImage!!,
            onDismiss = { fullScreenImage = null }
        )
    }
}

@Composable
fun PhotoCard(
    imageBitmap: ImageBitmap,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(AdditionalTheme.spacings.small)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(AdditionalTheme.roundness.normalPercent),
    ) {
        Image(
            bitmap = imageBitmap,
            contentDescription = stringResource(Res.string.file_cabinet_photo_card),
            modifier = Modifier
                .fillMaxWidth()
                .height(AdditionalTheme.sizing.fileCabinetNormal)
                .padding(AdditionalTheme.spacings.small)
                .clip(RoundedCornerShape(AdditionalTheme.roundness.normalPercent)),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun LoadingCard() {
    Card(
        modifier = Modifier
            .padding(AdditionalTheme.spacings.medium)
            .fillMaxWidth()
            .height(AdditionalTheme.sizing.fileCabinetNormal),
        shape = RoundedCornerShape(AdditionalTheme.roundness.normalPercent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
        )
    }
}