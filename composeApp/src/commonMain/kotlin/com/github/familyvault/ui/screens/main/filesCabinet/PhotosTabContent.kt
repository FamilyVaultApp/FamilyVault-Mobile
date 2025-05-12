package com.github.familyvault.ui.screens.main.filesCabinet

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import com.github.familyvault.services.IFileCabinetService
import com.github.familyvault.services.IImagePickerService
import com.github.familyvault.ui.components.FullScreenImage
import com.github.familyvault.ui.components.LoaderWithText
import com.github.familyvault.ui.components.filesCabinet.LoadingCard
import com.github.familyvault.ui.components.filesCabinet.PhotoCard
import com.github.familyvault.ui.theme.AdditionalTheme
import com.github.familyvault.utils.TimeFormatter
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.loading
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun PhotosTabContent() {
    val fileCabinetService = koinInject<IFileCabinetService>()
    val imagePicker = koinInject<IImagePickerService>()
    val storeId = fileCabinetService.retrieveFileCabinetStoreId()

    var imageData by remember { mutableStateOf<List<Pair<ByteArray, Long>>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var fullScreenImage by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(Unit) {
        isLoading = true
        imageData = withContext(Dispatchers.IO) {
            fileCabinetService.getImagesFromFamilyGroupStoreAsByteArray(
                storeId = storeId,
                limit = 30,
                skip = 0
            )
        }
        isLoading = false
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            LoaderWithText(
                stringResource(Res.string.loading),
                modifier = Modifier.fillMaxSize()
            )
        }
    } else {
        val groupedByDate = remember(imageData) {
            imageData
                .sortedByDescending { it.second }
                .groupBy { (_, timestamp) -> TimeFormatter.formatDate(timestamp) }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.small),
            horizontalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.small),
            contentPadding = PaddingValues(AdditionalTheme.spacings.small)
        ) {
            groupedByDate.forEach { (date, images) ->
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Text(
                        text = date,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(AdditionalTheme.spacings.medium)
                    )
                }

                gridItems(images) { (imageBytes, _) ->
                    val imageBitmapState =
                        produceState<ImageBitmap?>(initialValue = null, imageBytes) {
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
    }

    fullScreenImage?.let { image ->
        FullScreenImage(
            imageBitmap = image,
            onDismiss = { fullScreenImage = null }
        )
    }
}
