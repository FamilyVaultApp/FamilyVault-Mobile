package com.github.familyvault.services

import android.content.Context
import android.database.Cursor
import android.provider.OpenableColumns
import android.util.Log
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
    private val TAG = "DocumentPickerService"

    fun initializeWithActivity(activity: ComponentActivity) {
        context = activity
        pickDocumentLauncher = activity.registerForActivityResult(
            ActivityResultContracts.OpenMultipleDocuments()
        ) { uris ->
            Log.d(TAG, "Document picker returned ${uris.size} URIs")
            clearSelectedDocuments()
            selectedDocumentUrls.addAll(uris.map { u -> 
                val uriStr = u.toString()
                val name = getDocumentNameFromUri(uriStr) ?: "unknown"
                val mime = getDocumentMimeTypeFromUri(uriStr) ?: "unknown"
                Log.d(TAG, "Selected document: $name ($mime) - URI: $uriStr")
                uriStr
            })

            val byteArrays = getSelectedDocumentAsByteArrays()
            Log.d(TAG, "Converted ${byteArrays.size} documents to byte arrays")
            byteArrays.forEachIndexed { index, bytes ->
                Log.d(TAG, "Document $index size: ${bytes.size} bytes")
            }

            continuation?.resume(byteArrays)
            continuation = null
        }
        isInitialized = true
        Log.d(TAG, "Document picker service initialized")
    }

    override fun openDocumentPicker() {
        if (!isInitialized) {
            Log.e(TAG, "DocumentPickerService not initialized!")
            throw IllegalStateException("DocumentPickerService not initialized. Call initializeWithActivity first.")
        }
        
        Log.d(TAG, "Opening document picker with mime types: PDF, text, Word")
        pickDocumentLauncher.launch(arrayOf(
            "*/*" // Allow all file types for better compatibility
        ))
    }

    override suspend fun pickDocumentsAndReturnByteArrays(): List<ByteArray> = suspendCoroutine { cont ->
        Log.d(TAG, "Called pickDocumentsAndReturnByteArrays")
        continuation = cont
        try {
            openDocumentPicker()
        } catch (e: Exception) {
            Log.e(TAG, "Error in pickDocumentsAndReturnByteArrays", e)
            cont.resume(emptyList())
        }
    }

    override fun getBytesFromUri(uriString: String): ByteArray? {
        return try {
            val result = context.contentResolver.openInputStream(uriString.toUri())?.use {
                it.readBytes()
            }
            Log.d(TAG, "Read ${result?.size ?: 0} bytes from URI: $uriString")
            result
        } catch (e: Exception) {
            Log.e(TAG, "Error reading bytes from URI: $uriString", e)
            null
        }
    }

    override fun getSelectedDocumentAsByteArrays(): List<ByteArray> {
        val bytes = selectedDocumentUrls.mapNotNull { uriString ->
            getBytesFromUri(uriString)
        }
        Log.d(TAG, "getSelectedDocumentAsByteArrays returning ${bytes.size} document byte arrays")
        return bytes
    }

    override fun getSelectedDocumentUrls(): List<String> = selectedDocumentUrls

    override fun clearSelectedDocuments() {
        selectedDocumentUrls.clear()
        Log.d(TAG, "Cleared selected documents")
    }

    override fun removeSelectedDocument(uri: String) {
        selectedDocumentUrls.remove(uri)
        Log.d(TAG, "Removed document with URI: $uri")
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
        Log.d(TAG, "Document name from URI $uriString: $result")
        return result
    }
    
    override fun getDocumentMimeTypeFromUri(uriString: String): String? {
        val uri = uriString.toUri()
        val mimeType = context.contentResolver.getType(uri)
        Log.d(TAG, "MIME type for URI $uriString: $mimeType")
        return mimeType
    }
}
