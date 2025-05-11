package com.github.familyvault.services

import com.github.familyvault.models.fileCabinet.FileCabinetDocument

interface IFileCabinetService {
    suspend fun createInitialStores()

    fun sendImageToFileCabinetGallery(image: ByteArray)
    suspend fun sendDocumentToFileCabinetDocuments(
        content: ByteArray,
        name: String,
        mimeType: String
    )

    fun getImagesFromFileCabinetGallery(): List<ByteArray>

    fun getDocumentsFromFileCabinetDocuments(): List<FileCabinetDocument>

    suspend fun restoreFileCabinetMembership()

    fun getGalleryStoreId(): String
}