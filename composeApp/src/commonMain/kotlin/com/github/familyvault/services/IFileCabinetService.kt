package com.github.familyvault.services

import com.github.familyvault.backend.models.ThreadItem
import com.github.familyvault.models.DocumentWithMetadata

interface IFileCabinetService {
    suspend fun createInitialStores()

    fun retrieveFileCabinetImagesThread(): ThreadItem
    fun retrieveFileCabinetDocumentsThread(): ThreadItem
    
    fun retrieveFileCabinetImagesStoreId(): String
    fun retrieveFileCabinetDocumentsStoreId(): String

    suspend fun createDocumentsStoreIfNotExists()
    
    fun sendImageToFamilyGroupStore(imageByteArray: ByteArray)
    suspend fun sendDocumentToFamilyGroupStore(
        documentByteArray: ByteArray,
        documentName: String,
        documentMimeType: String
    )
    
    fun getDocumentsFromFamilyGroupStore(storeId: String?, limit: Long, skip: Long): List<Pair<ByteArray, Long>>

    fun getImagesFromFamilyGroupStoreAsByteArray(storeId: String?, limit: Long, skip: Long) : List<Pair<ByteArray, Long>>
    fun getDocumentsWithMetadataFromStore(storeId: String?, limit: Long, skip: Long): List<DocumentWithMetadata>

    suspend fun restoreFileCabinetMembership()
}