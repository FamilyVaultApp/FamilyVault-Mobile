package com.github.familyvault.services

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.ImageBitmap
import com.github.familyvault.AppConfig
import com.github.familyvault.models.ImageSize
import org.jetbrains.skia.Bitmap
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ImagePickerService : IImagePickerService {
    private var continuation: Continuation<List<ByteArray>>? = null
    private val selectedImageUrls = mutableStateListOf<String>()

    fun initializeWithActivity() {
        TODO("Method not yet implemented")
    }

    override fun openMediaPickerForSelectingImages() {
        TODO("Method not yet implemented")
    }

    override suspend fun pickImagesAndReturnByteArrays(): List<ByteArray> = suspendCoroutine { cont ->
        TODO("Method not yet implemented")
    }

    override fun getBytesFromUri(uriString: String): ByteArray? {
        TODO("Method not yet implemented")
    }

    override fun getSelectedImageAsByteArrays(): List<ByteArray> {
        TODO("Method not yet implemented")
    }

    override fun getSelectedImageUrls(): List<String> = selectedImageUrls

    override fun getBitmapFromBytes(imageBytes: ByteArray): ImageBitmap {
        TODO("Method not yet implemented")
    }

    override fun clearSelectedImages() {
        TODO("Method not yet implemented")
    }

    override fun removeSelectedImage(uri: String) {
        TODO("Method not yet implemented")
    }

    override fun compressAndRotateImage(
        imageByteArray: ByteArray,
        compressionQuality: Int?
    ): ByteArray {
        TODO("Method not yet implemented")
    }

    override fun getImageAsByteArraySize(image: ByteArray): ImageSize {
        TODO("Method not yet implemented")
    }

    private fun compressImage(bitmap: Bitmap, quality: Int): ByteArray {
        TODO("Method not yet implemented")
    }

    private fun fixImageRotation(imageBytes: ByteArray): Bitmap {
        TODO("Method not yet implemented")
    }

}