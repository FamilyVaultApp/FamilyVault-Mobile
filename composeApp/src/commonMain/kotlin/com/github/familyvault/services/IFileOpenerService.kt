package com.github.familyvault.services

interface IFileOpenerService {
    /**
     * Opens a file with an external viewer based on its content type.
     * 
     * @param fileBytes The file content as byte array
     * @param mimeType The MIME type of the file content
     * @param fileName The name of the file (used for creating temporary files)
     * @return Whether the file was successfully opened
     */
    fun openFileWithExternalViewer(fileBytes: ByteArray, mimeType: String, fileName: String): Boolean
    
    /**
     * Downloads a file to the device's Downloads folder.
     *
     * @param fileBytes The file content as byte array
     * @param fileName The name to save the file with
     * @return The path to the downloaded file if successful, null otherwise
     */
    fun downloadFile(fileBytes: ByteArray, fileName: String): String?
    
    /**
     * Determines if a byte array contains PDF data.
     * 
     * @param fileBytes The file content to check
     * @return True if the bytes represent a PDF file
     */
    fun isPdfFile(fileBytes: ByteArray): Boolean
}
