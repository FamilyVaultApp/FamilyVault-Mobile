package com.github.familyvault.services

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.ImageBitmap
import platform.UIKit.UIApplication
import platform.UIKit.UIDocumentPickerMode
import platform.UIKit.UIDocumentPickerViewController
import platform.UIKit.UIDocumentViewController
import platform.UniformTypeIdentifiers.UTType
import platform.UniformTypeIdentifiers.UTTypeText
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DocumentPickerService : IDocumentPickerService {
    private var continuation: Continuation<List<ByteArray>>? = null
    private val selectedDocumentUrls = mutableStateListOf<String>()
    private var isInitialized = false
    private val documentPickerController = UIDocumentPickerViewController(
        documentTypes = listOf(
            UTTypeText
        ),
        UIDocumentPickerMode.UIDocumentPickerModeImport
    )

    companion object {
        private const val TAG = "DocumentPickerService"
    }

    fun initializeWithActivity() {
    }

    override fun openDocumentPicker() {
        UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
            documentPickerController,
            false
        ) {

        }
    }

    override suspend fun pickDocumentsAndReturnByteArrays(): List<ByteArray> =
        suspendCoroutine { cont ->
//            Log.d(TAG, "Called pickDocumentsAndReturnByteArrays")
            println("Open documentPicker")
            continuation = cont
            try {
                println("Open documentPicker")
                openDocumentPicker()
            } catch (e: Exception) {
//                Log.e(TAG, "Error in pickDocumentsAndReturnByteArrays", e)
                cont.resume(emptyList())
            }
        }

    override fun getBytesFromUri(uriString: String): ByteArray? {
        TODO("Method not yet implemented")
    }

    override fun getSelectedDocumentAsByteArrays(): List<ByteArray> {
        TODO("Method not yet implemented")
    }

    override fun getSelectedDocumentUrls(): List<String> = selectedDocumentUrls

    override fun clearSelectedDocuments() {
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
