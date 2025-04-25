package com.github.familyvault.services

import androidx.compose.ui.graphics.ImageBitmap

interface IMediaPickerService {
    val selectedMediaUrl: MutableList<String>

    fun pickMedia()
    fun getBytesFromUri(uriString: String): ByteArray?
    fun getBitmapFromBytes(imageBytes: ByteArray): ImageBitmap?
}