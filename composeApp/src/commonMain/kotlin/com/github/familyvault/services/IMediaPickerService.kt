package com.github.familyvault.services

import androidx.compose.ui.graphics.ImageBitmap

interface IMediaPickerService {
    fun openMediaPickerForSelectingMedia()
    fun getBytesFromUri(uriString: String): ByteArray?
    fun getBitmapFromBytes(imageBytes: ByteArray): ImageBitmap
    fun getSelectedMediaAsByteArrays(): List<ByteArray>
    fun getSelectedMedialUrls(): List<String>
    fun compressImage(imageByteArray: ByteArray, quality: Int): ByteArray
    fun clearSelectedMedia()
    fun removeSelectedMedia(uri: String)
}