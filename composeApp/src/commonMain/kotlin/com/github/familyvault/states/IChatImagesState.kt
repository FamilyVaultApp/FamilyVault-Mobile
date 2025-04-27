package com.github.familyvault.states

import androidx.compose.ui.graphics.ImageBitmap

interface IChatImagesState {
    fun storeImage(id: String, content: ImageBitmap)
    fun getImageOrNull(id: String): ImageBitmap?
}