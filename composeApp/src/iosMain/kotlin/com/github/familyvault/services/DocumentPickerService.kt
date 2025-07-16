package com.github.familyvault.services

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.ImageBitmap
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DocumentPickerService : IDocumentPickerService {
    private var continuation: Continuation<List<ByteArray>>? = null
    private val selectedDocumentUrls = mutableStateListOf<String>()
    private var isInitialized = false

    companion object {
        private const val TAG = "DocumentPickerService"
    }

    fun initializeWithActivity() {
        TODO("Method not yet implemented")
    }

    override fun openDocumentPicker() {
        TODO("Method not yet implemented")
    }

    override suspend fun pickDocumentsAndReturnByteArrays(): List<ByteArray> =
        TODO("Method not yet implemented")

    override fun getBytesFromUri(uriString: String): ByteArray? {
        TODO("Method not yet implemented")
    }

    override fun getSelectedDocumentAsByteArrays(): List<ByteArray> {
        TODO("Method not yet implemented")
    }

    override fun getSelectedDocumentUrls(): List<String> = selectedDocumentUrls

    override fun clearSelectedDocuments() {
        TODO("Method not yet implemented")
    }

    override fun removeSelectedDocument(uri: String) {
        TODO("Method not yet implemented")
    }

    override fun getDocumentNameFromUri(uriString: String): String? {
        TODO("Method not yet implemented")
    }

    override fun getDocumentMimeTypeFromUri(uriString: String): String? {
        TODO("Method not yet implemented")
    }

    override fun getDocumentPreviewPageFromUri(uriString: String): ByteArray {
        TODO("Method not yet implemented")
    }
}
