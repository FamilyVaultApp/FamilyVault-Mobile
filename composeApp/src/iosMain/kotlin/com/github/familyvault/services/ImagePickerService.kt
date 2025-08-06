package com.github.familyvault.services

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.ImageBitmap
import com.github.familyvault.AppConfig
import com.github.familyvault.models.ImageSize
import org.jetbrains.skia.Bitmap
import platform.Foundation.NSCoder
import platform.UIKit.UIApplication
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIViewController

class ImagePickerService : IImagePickerService {
    private var continuation: Continuation<List<ByteArray>>? = null
    private val selectedImageUrls = mutableStateListOf<String>()
    private val pickerController = UIImagePickerController()
    fun initializeWithActivity() {

    }

    override fun openMediaPickerForSelectingImages() {
        UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
            pickerController,
            false
        ) {

        }
    }

    override suspend fun pickImagesAndReturnByteArrays(): List<ByteArray> =
        suspendCoroutine { cont ->
        }

    override fun getBytesFromUri(uriString: String): ByteArray? {
        return null
        TODO("Method not yet implemented")
    }

    override fun getSelectedImageAsByteArrays(): List<ByteArray> {
//        emptyList()
        TODO("Method not yet implemented")
    }

    override fun getSelectedImageUrls(): List<String> = selectedImageUrls

    override fun getBitmapFromBytes(imageBytes: ByteArray): ImageBitmap {
        TODO("Method not yet implemented")
    }

    override fun clearSelectedImages() {
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