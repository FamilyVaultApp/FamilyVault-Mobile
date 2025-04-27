package com.github.familyvault.services

import androidx.compose.ui.graphics.ImageBitmap

interface IMediaPickerService {
    val selectedMediaUrls: MutableList<String>

    fun openMediaPickerForSelectingMedia()
    fun getBytesFromUri(uriString: String): ByteArray?
    fun getBitmapFromBytes(imageBytes: ByteArray): ImageBitmap
    fun getSelectedMediaAsByteArrays(): List<ByteArray>
    fun clearSelectedMedia()
    fun removeSelectedMedia(uri: String)
}