package com.github.familyvault.services

interface IFileOpenerService {
    fun openFileWithExternalViewer(fileBytes: ByteArray, mimeType: String, fileName: String): Boolean
    
    fun downloadFile(fileBytes: ByteArray, fileName: String): String?
    
    fun isPdfFile(fileBytes: ByteArray): Boolean
}
