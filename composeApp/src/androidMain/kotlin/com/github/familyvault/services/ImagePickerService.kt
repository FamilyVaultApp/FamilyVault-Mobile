package com.github.familyvault.services

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.net.toUri
import com.github.familyvault.AppConfig
import com.github.familyvault.models.ImageSize
import java.io.ByteArrayOutputStream
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ImagePickerService : IImagePickerService {
    private var continuation: Continuation<List<ByteArray>>? = null
    private lateinit var pickFileLauncher: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var context: Context
    private val selectedImageUrls = mutableStateListOf<String>()

    fun initializeWithActivity(activity: ComponentActivity) {
        context = activity
        pickFileLauncher = activity.registerForActivityResult(
            ActivityResultContracts.PickMultipleVisualMedia()
        ) {
            clearSelectedImages()
            selectedImageUrls.addAll(it.map { u -> u.toString() })

            continuation?.resume(getSelectedImageAsByteArrays())
            continuation = null
        }
    }

    override fun openMediaPickerForSelectingImages() {
        pickFileLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    override suspend fun pickImagesAndReturnByteArrays(): List<ByteArray> = suspendCoroutine { cont ->
        continuation = cont
        openMediaPickerForSelectingImages()
    }

    override fun getBytesFromUri(uriString: String): ByteArray? {
        return context.contentResolver.openInputStream(uriString.toUri())?.use {
            it.readBytes()
        }
    }

    override fun getSelectedImageAsByteArrays(): List<ByteArray> {
        val bytes = selectedImageUrls.mapNotNull { uriString ->
            getBytesFromUri(uriString)
        }
        clearSelectedImages()
        return bytes
    }

    override fun getSelectedImageUrls(): List<String> = selectedImageUrls

    override fun getBitmapFromBytes(imageBytes: ByteArray): ImageBitmap {
        val rotatedBitmap = fixImageRotation(imageBytes)
        return rotatedBitmap.asImageBitmap()
    }

    override fun clearSelectedImages() {
        selectedImageUrls.clear()
    }

    override fun removeSelectedImage(uri: String) {
        selectedImageUrls.remove(uri)
    }

    override fun compressAndRotateImage(
        imageByteArray: ByteArray,
        compressionQuality: Int?
    ): ByteArray {
        val rotatedImage = fixImageRotation(imageByteArray)

        return compressImage(
            rotatedImage,
            compressionQuality ?: AppConfig.DEFAULT_COMPRESSION_QUALITY
        )
    }

    override fun getImageAsByteArraySize(image: ByteArray): ImageSize {
        val imageBitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
        return ImageSize(imageBitmap.height, imageBitmap.width)
    }

    private fun compressImage(bitmap: Bitmap, quality: Int): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()

        bitmap.compress(
            Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream
        )

        return byteArrayOutputStream.toByteArray()
    }

    private fun fixImageRotation(imageBytes: ByteArray): Bitmap {
        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        try {
            val exif = ExifInterface(imageBytes.inputStream())
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL
            )

            val matrix = Matrix()

            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
                ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.postScale(-1f, 1f)
                ExifInterface.ORIENTATION_FLIP_VERTICAL -> matrix.postScale(1f, -1f)
            }

            return Bitmap.createBitmap(
                bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return bitmap
        }
    }

}