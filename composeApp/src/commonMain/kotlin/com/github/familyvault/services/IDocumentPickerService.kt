package com.github.familyvault.services

interface IDocumentPickerService {
    fun openDocumentPicker()
    suspend fun pickDocumentsAndReturnByteArrays(): List<ByteArray>
    fun getBytesFromUri(uriString: String): ByteArray?
    fun getSelectedDocumentAsByteArrays(): List<ByteArray>
    fun getSelectedDocumentUrls(): List<String>
    fun clearSelectedDocuments()
    fun removeSelectedDocument(uri: String)
    fun getDocumentNameFromUri(uriString: String): String?
    fun getDocumentMimeTypeFromUri(uriString: String): String?
    fun getDocumentPreviewPageFromUri(uriString: String): ByteArray
}
