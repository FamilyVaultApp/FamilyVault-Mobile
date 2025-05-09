package com.github.familyvault.services

import com.github.familyvault.backend.models.ThreadItem
import com.github.familyvault.models.DocumentWithMetadata

interface IFileCabinetService {
    suspend fun createInitialStores()
    
    // Original methods - keep for backward compatibility
    fun retrieveFileCabinetThread(): ThreadItem
    fun retrieveFileCabinetStoreId(): String
    
    // New methods for separate stores
    fun retrieveFileCabinetImagesThread(): ThreadItem
    fun retrieveFileCabinetDocumentsThread(): ThreadItem
    
    fun retrieveFileCabinetImagesStoreId(): String
    fun retrieveFileCabinetDocumentsStoreId(): String
    
    // Method to ensure documents store exists (will create if missing)
    suspend fun ensureDocumentsStoreExists()
    
    fun sendImageToFamilyGroupStore(imageByteArray: ByteArray)
    suspend fun sendDocumentToFamilyGroupStore(
        documentByteArray: ByteArray,
        documentName: String,
        documentMimeType: String
    )
    
    fun getImagesFromFamilyGroupStoreAsByteArray(storeId: String?, limit: Long, skip: Long): List<ByteArray>
    fun getDocumentsFromFamilyGroupStore(storeId: String?, limit: Long, skip: Long): List<ByteArray>
    
    /**
     * Get documents with their metadata from the family group store
     */
    fun getDocumentsWithMetadataFromStore(storeId: String?, limit: Long, skip: Long): List<DocumentWithMetadata>
    
    suspend fun restoreFileCabinetMembership()
}