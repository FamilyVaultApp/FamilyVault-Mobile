package com.github.familyvault.services

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateListOf

class MediaPickerService : IMediaPickerService {
    private lateinit var pickFileLauncher: ActivityResultLauncher<PickVisualMediaRequest>
    override var selectedMediaUrl: MutableList<String> = mutableStateListOf()
        private set

    fun initializeWithActivity(activity: ComponentActivity) {
        pickFileLauncher =
            activity.registerForActivityResult(
                ActivityResultContracts.PickMultipleVisualMedia(5)
            ) {
                selectedMediaUrl.clear()
                selectedMediaUrl.addAll(it.map { u -> u.toString() })
            }
    }

    override fun pickMedia() {
        pickFileLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
    }
}