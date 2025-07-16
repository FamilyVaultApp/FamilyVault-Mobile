package com.github.familyvault.services

import com.github.familyvault.utils.FileTypeUtils
import com.simplito.kotlin.privmx_endpoint.model.File

class FileOpenerService() : IFileOpenerService {
    private val TAG = "FileOpenerService"
    
    override fun openFileWithExternalViewer(
        fileBytes: ByteArray,
        mimeType: String,
        fileName: String
    ): Boolean {
        TODO("Method not yet implemented")
    }
    
    override fun downloadFile(fileBytes: ByteArray, fileName: String): String? {
        TODO("Method not yet implemented")
    }
    
    private fun createTempFile(fileBytes: ByteArray, fileName: String): File {
        TODO("Method not yet implemented")
    }
}
