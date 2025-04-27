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

class MediaPickerService : IMediaPickerService {
    private lateinit var pickFileLauncher: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var context: Context
    override val selectedMediaUrls = mutableStateListOf<String>()

    fun initializeWithActivity(activity: ComponentActivity) {
        context = activity
        pickFileLauncher =
            activity.registerForActivityResult(
                ActivityResultContracts.PickMultipleVisualMedia()
            ) {
                clearSelectedMedia()
                selectedMediaUrls.addAll(it.map { u -> u.toString() })
            }
    }

    override fun openMediaPickerForSelectingMedia() {
        pickFileLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    override fun getBytesFromUri(uriString: String): ByteArray? {
        return context.contentResolver.openInputStream(uriString.toUri())?.use {
            it.readBytes()
        }
    }

    override fun getSelectedMediaAsByteArrays(): List<ByteArray> {
        val bytes = selectedMediaUrls.mapNotNull { uriString ->
            getBytesFromUri(uriString)
        }
        clearSelectedMedia()
        return bytes
    }

    override fun getBitmapFromBytes(imageBytes: ByteArray): ImageBitmap {
        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        val rotatedBitmap = fixImageRotation(bitmap, imageBytes)
        return rotatedBitmap.asImageBitmap()
    }

    override fun clearSelectedMedia() {
        selectedMediaUrls.clear()
    }

    override fun removeSelectedMedia(uri: String) {
        selectedMediaUrls.remove(uri)
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