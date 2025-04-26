package com.github.familyvault.services

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

class MediaPickerService : IMediaPickerService {
    private lateinit var pickFileLauncher: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var context: ComponentActivity
    override var selectedMediaUrl: MutableList<String> = mutableStateListOf()
        private set

    fun initializeWithActivity(activity: ComponentActivity) {
        context = activity
        pickFileLauncher =
            activity.registerForActivityResult(
                ActivityResultContracts.PickMultipleVisualMedia()
            ) {
                selectedMediaUrl.clear()
                selectedMediaUrl.addAll(it.map { u -> u.toString() })
            }
    }

    override fun pickMedia() {
        pickFileLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    override fun getBytesFromUri(uriString: String): ByteArray? {
        val uri = Uri.parse(uriString)
        return context.contentResolver.openInputStream(uri)?.use {
            it.readBytes()
        }
    }

    override fun getBitmapFromBytes(imageBytes: ByteArray): ImageBitmap? {
        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        return bitmap?.asImageBitmap()
    }
}