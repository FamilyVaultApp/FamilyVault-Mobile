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
import java.io.ByteArrayOutputStream

class ImagePickerService : IImagePickerService {
    private lateinit var pickFileLauncher: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var context: Context
    private val selectedImageUrls = mutableStateListOf<String>()

    fun initializeWithActivity(activity: ComponentActivity) {
        context = activity
        pickFileLauncher =
            activity.registerForActivityResult(
                ActivityResultContracts.PickMultipleVisualMedia()
            ) {
                clearSelectedImages()
                selectedImageUrls.addAll(it.map { u -> u.toString() })
            }
    }

    override fun openMediaPickerForSelectingImages() {
        pickFileLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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
        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        val rotatedBitmap = fixImageRotation(bitmap, imageBytes)
        return rotatedBitmap.asImageBitmap()
    }

    override fun clearSelectedImages() {
        selectedImageUrls.clear()
    }

    override fun removeSelectedImage(uri: String) {
        selectedImageUrls.remove(uri)
    }

    override fun compressImage(imageByteArray: ByteArray, quality: Int): ByteArray {
        val bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)

        val rotatedBitmap = fixImageRotation(bitmap, imageByteArray)

        val byteArrayOutputStream = ByteArrayOutputStream()
        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)

        return byteArrayOutputStream.toByteArray()
    }

    private fun fixImageRotation(bitmap: Bitmap, imageBytes: ByteArray): Bitmap {
        return try {
            val exif = ExifInterface(imageBytes.inputStream())
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )

            val matrix = Matrix()

            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
                ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.postScale(-1f, 1f)
                ExifInterface.ORIENTATION_FLIP_VERTICAL -> matrix.postScale(1f, -1f)
            }

            Bitmap.createBitmap(
                bitmap,
                0,
                0,
                bitmap.width,
                bitmap.height,
                matrix,
                true
            )
        } catch (e: Exception) {
            e.printStackTrace()
            bitmap
        }
    }
}