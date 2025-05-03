package com.github.familyvault.ui.screens.main.filesCabinet

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.ui.graphics.ImageBitmap
import com.github.familyvault.services.IImagePickerService
import org.koin.compose.koinInject

@Composable
fun PhotosTabContent() {
    val imagePicker = koinInject<IImagePickerService>()

    val imageUrls = imagePicker.getSelectedImageUrls()

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(imageUrls.size) { index ->
            val imageBytes = imagePicker.getBytesFromUri(imageUrls[index])
            imageBytes?.let {
                val imageBitmap = imagePicker.getBitmapFromBytes(it)
                PhotoCard(imageBitmap = imageBitmap)
            }
        }
    }
}

@Composable
fun PhotoCard(imageBitmap: ImageBitmap) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
    ) {
        Image(
            bitmap = imageBitmap,
            contentDescription = "Photo",
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(4.dp)
        )
    }
}