package com.github.familyvault.services

import androidx.compose.ui.graphics.ImageBitmap
import com.github.familyvault.models.ImageSize

interface IImagePickerService {
    fun openMediaPickerForSelectingImages()
    suspend fun pickImagesAndReturnByteArrays(): List<ByteArray>
    fun getBytesFromUri(uriString: String): ByteArray?
    fun getBitmapFromBytes(imageBytes: ByteArray): ImageBitmap
    fun getSelectedImageAsByteArrays(): List<ByteArray>
    fun getSelectedImageUrls(): List<String>
    fun compressAndRotateImage(
        imageByteArray: ByteArray,
        compressionQuality: Int? = null
    ): ByteArray

    fun getImageAsByteArraySize(image: ByteArray): ImageSize
    fun clearSelectedImages()
    fun removeSelectedImage(uri: String)
}