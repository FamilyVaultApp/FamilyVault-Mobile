package com.github.familyvault.states

import androidx.compose.ui.graphics.ImageBitmap

class ChatImagesState : IChatImagesState {
    private val imageCache: MutableMap<String, ImageBitmap> = mutableMapOf()

    override fun storeImage(id: String, content: ImageBitmap) {
        imageCache[id] = content
    }

    override fun getImageOrNull(id: String): ImageBitmap? {
        return imageCache[id]
    }
}