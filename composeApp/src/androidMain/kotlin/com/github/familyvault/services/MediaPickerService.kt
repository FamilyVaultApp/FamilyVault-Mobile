package com.github.familyvault.services

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateListOf

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
        pickFileLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
    }

    override fun getBytesFromUri(uriString: String): ByteArray? {
        val uri = Uri.parse(uriString)
        return context.contentResolver.openInputStream(uri)?.use {
            it.readBytes()
        }
    }
}