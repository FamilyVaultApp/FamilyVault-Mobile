package com.github.familyvault.services

import com.github.familyvault.AppConfig
import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.backend.models.ThreadItem
import com.github.familyvault.models.DocumentWithMetadata
import com.github.familyvault.models.enums.StoreType
import com.github.familyvault.models.enums.fileCabinet.FileCabinetThreadType
import com.github.familyvault.utils.FamilyMembersSplitter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class FileCabinetService(
    private val privMxClient: IPrivMxClient,
    private val imagePickerService: IImagePickerService,
    private val familyGroupSessionService: IFamilyGroupSessionService,
    private val familyGroupService: IFamilyGroupService
) : IFileCabinetService {
    
    companion object {
        private const val IMAGES_SUFFIX = " - Images"
        private const val DOCUMENTS_SUFFIX = " - Documents"
    }
    
    override suspend fun createInitialStores() {
        val contextId = familyGroupSessionService.getContextId()
        val familyGroupName = familyGroupSessionService.getFamilyGroupName()
        val splitFamilyMembers = FamilyMembersSplitter.split(
            familyGroupService.retrieveFamilyGroupMembersList()
        )

        val users = splitFamilyMembers.members.map { it.toPrivMxUser() }
        val managers = splitFamilyMembers.guardians.map { it.toPrivMxUser() }

        // Create images store and thread
        val imagesStoreId = privMxClient.createStore(
            contextId,
            users,
            managers,
            StoreType.FILE_CABINET_IMAGES.toString()
        )

        privMxClient.createThread(
            contextId = contextId,
            users = users,
            managers = managers,
            tag = AppConfig.FILE_CABINET_THREAD_TAG,
            type = FileCabinetThreadType.IMAGES.toString(),
            name = "$familyGroupName$IMAGES_SUFFIX",
            referenceStoreId = imagesStoreId,
            threadInitialCreators = emptyList()
        )
        
        // Create documents store and thread
        val documentsStoreId = privMxClient.createStore(
            contextId,
            users,
            managers,
            StoreType.FILE_CABINET_DOCUMENTS.toString()
        )
        
        privMxClient.createThread(
            contextId = contextId,
            users = users,
            managers = managers,
            tag = AppConfig.FILE_CABINET_THREAD_TAG,
            type = FileCabinetThreadType.DOCUMENTS.toString(),
            name = "$familyGroupName$DOCUMENTS_SUFFIX",
            referenceStoreId = documentsStoreId,
            threadInitialCreators = emptyList()
        )
    }

    // Original method - keep for backward compatibility
    override fun retrieveFileCabinetThread(): ThreadItem {
        return retrieveFileCabinetImagesThread()
    }

    override fun retrieveFileCabinetImagesThread(): ThreadItem {
        val contextId = familyGroupSessionService.getContextId()
        val fileCabinetThreads = privMxClient.retrieveAllThreadsWithTag(
            contextId,
            AppConfig.FILE_CABINET_THREAD_TAG,
            startIndex = 0,
            pageSize = 100
        )

        // First, try to find a thread with Images suffix in the name
        val imagesThread = fileCabinetThreads.firstOrNull { 
            it.privateMeta.name.endsWith(IMAGES_SUFFIX)
        }

        // For backward compatibility, use the first thread if no suffixed thread is found
        // but ONLY if no documents thread exists either
        return imagesThread ?: fileCabinetThreads.firstOrNull { thread ->
            // Make sure we're not selecting a documents thread
            !thread.privateMeta.name.endsWith(DOCUMENTS_SUFFIX)
        } ?: throw IllegalStateException("No file cabinet images thread found")
    }
    
    override fun retrieveFileCabinetDocumentsThread(): ThreadItem {
        val contextId = familyGroupSessionService.getContextId()
        val fileCabinetThreads = privMxClient.retrieveAllThreadsWithTag(
            contextId,
            AppConfig.FILE_CABINET_THREAD_TAG,
            startIndex = 0,
            pageSize = 100
        )

        // Look specifically for a thread with Documents suffix
        return fileCabinetThreads.firstOrNull { 
            it.privateMeta.name.endsWith(DOCUMENTS_SUFFIX)
        } ?: throw IllegalStateException("No file cabinet documents thread found")
    }

    // Original method - keep for backward compatibility
    override fun retrieveFileCabinetStoreId(): String {
        return retrieveFileCabinetImagesStoreId()
    }
    
    override fun retrieveFileCabinetImagesStoreId(): String {
        return requireNotNull(retrieveFileCabinetImagesThread().privateMeta.referenceStoreId)
    }
    
    override fun retrieveFileCabinetDocumentsStoreId(): String {
        return requireNotNull(retrieveFileCabinetDocumentsThread().privateMeta.referenceStoreId)
    }

    /**
     * Ensures the documents store exists. If it doesn't exist, creates it.
     */
    override suspend fun ensureDocumentsStoreExists(): Unit = withContext(Dispatchers.IO) {
        try {
            // Try to access the documents thread
            retrieveFileCabinetDocumentsThread()
            // If no exception was thrown, the thread exists
        } catch (e: IllegalStateException) {
            // Thread doesn't exist, create it
            val contextId = familyGroupSessionService.getContextId()
            val familyGroupName = familyGroupSessionService.getFamilyGroupName()
            val splitFamilyMembers = FamilyMembersSplitter.split(
                familyGroupService.retrieveFamilyGroupMembersList()
            )

            val users = splitFamilyMembers.members.map { it.toPrivMxUser() }
            val managers = splitFamilyMembers.guardians.map { it.toPrivMxUser() }
            
            // Create documents store and thread
            val documentsStoreId = privMxClient.createStore(
                contextId,
                users,
                managers,
                StoreType.FILE_CABINET_DOCUMENTS.toString()
            )
            
            privMxClient.createThread(
                contextId = contextId,
                users = users,
                managers = managers,
                tag = AppConfig.FILE_CABINET_THREAD_TAG,
                type = FileCabinetThreadType.DOCUMENTS.toString(),
                name = "$familyGroupName$DOCUMENTS_SUFFIX",
                referenceStoreId = documentsStoreId,
                threadInitialCreators = emptyList()
            )
        }
    }

    override fun sendImageToFamilyGroupStore(
        imageByteArray: ByteArray
    ) {
        val storeId = retrieveFileCabinetImagesStoreId()
        val rotatedAndCompressedImage = imagePickerService.compressAndRotateImage(imageByteArray)
        privMxClient.sendByteArrayToStore(storeId, rotatedAndCompressedImage)
    }

    override fun getImagesFromFamilyGroupStoreAsByteArray(
        storeId: String?,
        limit: Long,
        skip: Long
    ): List<ByteArray> {
        val actualStoreId = storeId ?: retrieveFileCabinetImagesStoreId()
        return privMxClient.getFilesAsByteArrayFromStore(actualStoreId, limit, skip)
    }

    override suspend fun sendDocumentToFamilyGroupStore(
        documentByteArray: ByteArray,
        documentName: String,
        documentMimeType: String
    ) {
        try {
            val storeId = retrieveFileCabinetDocumentsStoreId()
            // Create metadata JSON to embed before the actual file content
            val metadata = """{"name":"$documentName","mime":"$documentMimeType","timestamp":${System.currentTimeMillis()}}"""
            
            // Prefix the document bytes with the metadata
            val metadataBytes = metadata.encodeToByteArray()
            val metadataLength = metadataBytes.size
            
            // Create a new byte array with: [metadataLength(4 bytes)][metadata bytes][document bytes]
            val combinedBytes = ByteArray(4 + metadataLength + documentByteArray.size)
            
            // Store metadata length as 4 bytes
            combinedBytes[0] = (metadataLength shr 24 and 0xFF).toByte()
            combinedBytes[1] = (metadataLength shr 16 and 0xFF).toByte() 
            combinedBytes[2] = (metadataLength shr 8 and 0xFF).toByte()
            combinedBytes[3] = (metadataLength and 0xFF).toByte()
            
            // Copy metadata bytes
            metadataBytes.copyInto(combinedBytes, 4)
            
            // Copy document bytes
            documentByteArray.copyInto(combinedBytes, 4 + metadataLength)
            
            privMxClient.sendByteArrayToStore(storeId, combinedBytes)
        } catch (e: Exception) {
            val contextId = familyGroupSessionService.getContextId()
            val familyGroupName = familyGroupSessionService.getFamilyGroupName()
            val splitFamilyMembers = FamilyMembersSplitter.split(
                familyGroupService.retrieveFamilyGroupMembersList()
            )

            val users = splitFamilyMembers.members.map { it.toPrivMxUser() }
            val managers = splitFamilyMembers.guardians.map { it.toPrivMxUser() }
            
            // Create documents store and thread
            val documentsStoreId = privMxClient.createStore(
                contextId,
                users,
                managers,
                StoreType.FILE_CABINET_DOCUMENTS.toString()
            )
            
            privMxClient.createThread(
                contextId = contextId,
                users = users,
                managers = managers,
                tag = AppConfig.FILE_CABINET_THREAD_TAG,
                type = FileCabinetThreadType.DOCUMENTS.toString(),
                name = "$familyGroupName$DOCUMENTS_SUFFIX",
                referenceStoreId = documentsStoreId,
                threadInitialCreators = emptyList()
            )
            
            // Now send to the newly created store
            privMxClient.sendByteArrayToStore(documentsStoreId, documentByteArray)
        }
    }

    override fun getDocumentsFromFamilyGroupStore(
        storeId: String?,
        limit: Long,
        skip: Long
    ): List<ByteArray> {
        val actualStoreId = storeId ?: retrieveFileCabinetDocumentsStoreId()
        return privMxClient.getFilesAsByteArrayFromStore(actualStoreId, limit, skip)
    }

    override fun getDocumentsWithMetadataFromStore(
        storeId: String?,
        limit: Long,
        skip: Long
    ): List<DocumentWithMetadata> {
        val actualStoreId = storeId ?: retrieveFileCabinetDocumentsStoreId()
        // Get the raw document bytes
        val rawBytes = privMxClient.getFilesAsByteArrayFromStore(actualStoreId, limit, skip)
        
        // Process each document, trying to extract metadata if present
        return rawBytes.mapIndexed { index, byteArray ->
            try {
                // Try to extract embedded metadata from bytes
                if (byteArray.size >= 4) {
                    // Get metadata length from first 4 bytes
                    val metadataLength = (byteArray[0].toInt() and 0xFF shl 24) or
                                        (byteArray[1].toInt() and 0xFF shl 16) or
                                        (byteArray[2].toInt() and 0xFF shl 8) or
                                        (byteArray[3].toInt() and 0xFF)
                    
                    // If we have valid metadata
                    if (metadataLength > 0 && metadataLength < byteArray.size - 4) {
                        val metadataBytes = byteArray.copyOfRange(4, 4 + metadataLength)
                        val metadataJson = metadataBytes.decodeToString()
                        
                        // Extract actual file content
                        val contentBytes = byteArray.copyOfRange(4 + metadataLength, byteArray.size)
                        
                        // Simple JSON parsing (we should use a proper JSON parser in production)
                        val nameMatch = Regex(""""name":"([^"]+)"""").find(metadataJson)
                        val mimeMatch = Regex(""""mime":"([^"]+)"""").find(metadataJson)
                        val timestampMatch = Regex(""""timestamp":(\d+)""").find(metadataJson)
                        
                        val fileName = nameMatch?.groupValues?.get(1)
                        val mimeType = mimeMatch?.groupValues?.get(1) 
                        val timestamp = timestampMatch?.groupValues?.get(1)?.toLongOrNull()
                        
                        return@mapIndexed DocumentWithMetadata(
                            content = contentBytes,
                            fileName = fileName,
                            mimeType = mimeType,
                            uploadDate = timestamp
                        )
                    }
                }
                
                // Fallback if metadata extraction fails
                // Try to infer file type from content
                val isPdf = isPdfFile(byteArray)
                val fileName = if (isPdf) "Document_${index}.pdf" else "File_${index}.jpg"
                val mimeType = if (isPdf) "application/pdf" else "image/jpeg"
                
                DocumentWithMetadata(
                    content = byteArray,
                    fileName = fileName,
                    mimeType = mimeType,
                    uploadDate = System.currentTimeMillis() - (index * 1000) // Fake timestamps
                )
            } catch (e: Exception) {
                // If anything goes wrong with metadata extraction, fall back to default behavior
                val isPdf = isPdfFile(byteArray)
                DocumentWithMetadata(
                    content = byteArray,
                    fileName = if (isPdf) "Document_${index}.pdf" else "File_${index}.jpg",
                    mimeType = if (isPdf) "application/pdf" else "image/jpeg",
                    uploadDate = System.currentTimeMillis() - (index * 1000)
                )
            }
        }
    }

    // Helper method to check if a byte array is a PDF file
    private fun isPdfFile(bytes: ByteArray): Boolean {
        return bytes.size >= 4 && 
               bytes[0].toInt() == 0x25 && // %
               bytes[1].toInt() == 0x50 && // P
               bytes[2].toInt() == 0x44 && // D
               bytes[3].toInt() == 0x46    // F
    }

    override suspend fun restoreFileCabinetMembership() {
        try {
            // Try to get and update both images and documents threads/stores
            val imagesThreadId = retrieveFileCabinetImagesThread().threadId
            val imagesStoreId = retrieveFileCabinetImagesStoreId()
            
            val splitFamilyMembers = FamilyMembersSplitter.split(
                familyGroupService.retrieveFamilyGroupMembersList()
            )

            val users = splitFamilyMembers.members.map { it.toPrivMxUser() }
            val managers = splitFamilyMembers.guardians.map { it.toPrivMxUser() }

            // Update images thread and store
            privMxClient.updateThread(imagesThreadId, users, managers)
            privMxClient.updateStore(imagesStoreId, users, managers)
            
            // Try to update documents thread and store if they exist
            try {
                val documentsThreadId = retrieveFileCabinetDocumentsThread().threadId
                val documentsStoreId = retrieveFileCabinetDocumentsStoreId()
                
                // Update documents thread and store
                privMxClient.updateThread(documentsThreadId, users, managers)
                privMxClient.updateStore(documentsStoreId, users, managers)
            } catch (e: Exception) {
                // Documents thread/store doesn't exist yet, that's okay
                // We'll create it when it's actually needed
            }
        } catch (e: Exception) {
            // Fallback to updating only images thread/store if documents aren't found
            val threadId = retrieveFileCabinetImagesThread().threadId
            val storeId = retrieveFileCabinetImagesStoreId()
            
            val splitFamilyMembers = FamilyMembersSplitter.split(
                familyGroupService.retrieveFamilyGroupMembersList()
            )

            val users = splitFamilyMembers.members.map { it.toPrivMxUser() }
            val managers = splitFamilyMembers.guardians.map { it.toPrivMxUser() }

            privMxClient.updateThread(threadId, users, managers)
            privMxClient.updateStore(storeId, users, managers)
        }
    }
}