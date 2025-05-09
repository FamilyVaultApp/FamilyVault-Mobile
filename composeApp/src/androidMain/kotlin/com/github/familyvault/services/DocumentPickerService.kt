package com.github.familyvault.services

import android.content.Context
import android.database.Cursor
import android.provider.OpenableColumns
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateListOf
import androidx.core.net.toUri
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DocumentPickerService : IDocumentPickerService {
    private var continuation: Continuation<List<ByteArray>>? = null
    private lateinit var pickDocumentLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var context: Context
    private val selectedDocumentUrls = mutableStateListOf<String>()
    private var isInitialized = false

    fun initializeWithActivity(activity: ComponentActivity) {
        context = activity
        pickDocumentLauncher = activity.registerForActivityResult(
            ActivityResultContracts.OpenMultipleDocuments()
        ) {
            clearSelectedDocuments()
            selectedDocumentUrls.addAll(it.map { u -> u.toString() })

            continuation?.resume(getSelectedDocumentAsByteArrays())
            continuation = null
        }
        isInitialized = true
    }

    override fun openDocumentPicker() {
        if (!isInitialized) {
            throw IllegalStateException("DocumentPickerService not initialized. Call initializeWithActivity first.")
        }
        
        pickDocumentLauncher.launch(arrayOf(
            "application/pdf", 
            "text/plain", 
            "application/msword", 
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        ))
    }

    override suspend fun pickDocumentsAndReturnByteArrays(): List<ByteArray> = suspendCoroutine { cont ->
        continuation = cont
        try {
            openDocumentPicker()
        } catch (e: Exception) {
            cont.resume(emptyList())
        }
    }

    override fun getBytesFromUri(uriString: String): ByteArray? {
        return context.contentResolver.openInputStream(uriString.toUri())?.use {
            it.readBytes()
        }
    }

    override fun getSelectedDocumentAsByteArrays(): List<ByteArray> {
        val bytes = selectedDocumentUrls.mapNotNull { uriString ->
            getBytesFromUri(uriString)
        }
        clearSelectedDocuments()
        return bytes
    }

    override fun getSelectedDocumentUrls(): List<String> = selectedDocumentUrls

    override fun clearSelectedDocuments() {
        selectedDocumentUrls.clear()
    }

    override fun removeSelectedDocument(uri: String) {
        selectedDocumentUrls.remove(uri)
    }
    
    override fun getDocumentNameFromUri(uriString: String): String? {
        val uri = uriString.toUri()
        var result: String? = null
        
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor: Cursor ->
            if (cursor.moveToFirst()) {
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    result = cursor.getString(nameIndex)
                }
            }
        }
        return result
    }
    
    override fun getDocumentMimeTypeFromUri(uriString: String): String? {
        val uri = uriString.toUri()
        return context.contentResolver.getType(uri)
    }
}
