package com.github.familyvault.ui.screens.main.filesCabinet

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.unit.dp
import com.github.familyvault.services.IFileCabinetService
import com.github.familyvault.services.IImagePickerService
import com.github.familyvault.services.listeners.IFileCabinetListenerService
import com.github.familyvault.ui.components.FullScreenImage
import com.github.familyvault.ui.components.LoaderWithText
import com.github.familyvault.ui.components.filesCabinet.LoadingCard
import com.github.familyvault.ui.components.filesCabinet.PhotoCard
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.file_cabinet_no_images
import familyvault.composeapp.generated.resources.file_cabinet_retry
import familyvault.composeapp.generated.resources.loading
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun PhotosTabContent() {
    val fileCabinetService = koinInject<IFileCabinetService>()
    val fileCabinetListenerService = koinInject<IFileCabinetListenerService>()
    val imagePicker = koinInject<IImagePickerService>()
    val coroutineScope = rememberCoroutineScope()

    val imageByteArrays = remember { mutableStateListOf<ByteArray>() }
    var isLoading by remember { mutableStateOf(true) }
    var fullScreenImage by remember { mutableStateOf<ImageBitmap?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    suspend fun loadImages() {
        isLoading = true
        errorMessage = null
        
        try {
            val storeId = fileCabinetService.retrieveFileCabinetImagesStoreId()

            imageByteArrays.clear()
            withContext(Dispatchers.IO) {
                imageByteArrays.addAll(fileCabinetService.getImagesFromFamilyGroupStoreAsByteArray(
                    storeId = storeId,
                    limit = 30,
                    skip = 0
                ))
            }
            fileCabinetListenerService.startListeningForNewFiles(storeId) {
                imageByteArrays.add(it)
            }
        } catch (e: Exception) {
            errorMessage = "Error loading images: ${e.message}"
            imageByteArrays.clear()
        } finally {
            isLoading = false
        }
    }

    LaunchedEffect(Unit) {
        loadImages()
    }

    DisposableEffect(Unit) {
        onDispose {
            fileCabinetListenerService.unregisterAllListeners()
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            LoaderWithText(
                stringResource(Res.string.loading), modifier = Modifier.fillMaxSize()
            )
        }
    } else if (errorMessage != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally, 
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(errorMessage!!)
                Button(onClick = { 
                    coroutineScope.launch {
                        loadImages()
                    }
                }) {
                    Text(stringResource(Res.string.file_cabinet_retry))
                }
            }
        }
    } else if (imageByteArrays.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(stringResource(Res.string.file_cabinet_no_images))
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