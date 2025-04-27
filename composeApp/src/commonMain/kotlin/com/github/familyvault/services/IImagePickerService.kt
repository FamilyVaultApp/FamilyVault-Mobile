package com.github.familyvault.services

import androidx.compose.ui.graphics.ImageBitmap

interface IImagePickerService {
    fun openMediaPickerForSelectingImages()
    fun getBytesFromUri(uriString: String): ByteArray?
    fun getBitmapFromBytes(imageBytes: ByteArray): ImageBitmap
    fun getSelectedImageAsByteArrays(): List<ByteArray>
    fun getSelectedImageUrls(): List<String>
    fun compressImage(imageByteArray: ByteArray, quality: Int): ByteArray
    fun clearSelectedImages()
    fun removeSelectedImage(uri: String)
}