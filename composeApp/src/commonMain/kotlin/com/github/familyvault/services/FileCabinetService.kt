package com.github.familyvault.services

import com.github.familyvault.AppConfig
import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.backend.models.ThreadItem
import com.github.familyvault.models.DocumentMetadata
import com.github.familyvault.models.DocumentWithMetadata
import com.github.familyvault.models.enums.StoreType
import com.github.familyvault.models.enums.fileCabinet.FileCabinetThreadType
import com.github.familyvault.utils.FamilyMembersSplitter
import com.github.familyvault.utils.FileTypeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class FileCabinetService(
    private val privMxClient: IPrivMxClient,
    private val imagePickerService: IImagePickerService,
    private val familyGroupSessionService: IFamilyGroupSessionService,
    private val familyGroupService: IFamilyGroupService
) : IFileCabinetService {
    
    companion object {
        private const val IMAGES_THREAD_NAME = "FILE_CABINET_IMAGES"
        private const val DOCUMENTS_THREAD_NAME = "FILE_CABINET_DOCUMENTS" 
    }
    
    override suspend fun createInitialStores() {
        val contextId = familyGroupSessionService.getContextId()
        val splitFamilyMembers = FamilyMembersSplitter.split(
            familyGroupService.retrieveFamilyGroupMembersList()
        )

        val users = splitFamilyMembers.members.map { it.toPrivMxUser() }
        val managers = splitFamilyMembers.guardians.map { it.toPrivMxUser() }

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
            name = IMAGES_THREAD_NAME,
            referenceStoreId = imagesStoreId,
            threadInitialCreators = emptyList()
        )

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
            name = DOCUMENTS_THREAD_NAME,
            referenceStoreId = documentsStoreId,
            threadInitialCreators = emptyList()
        )
    }

    override fun retrieveFileCabinetImagesThread(): ThreadItem {
        val contextId = familyGroupSessionService.getContextId()
        val fileCabinetThreads = privMxClient.retrieveAllThreadsWithTag(
            contextId,
            AppConfig.FILE_CABINET_THREAD_TAG,
            startIndex = 0,
            pageSize = 100
        )

        return fileCabinetThreads.firstOrNull { 
            it.privateMeta.name == IMAGES_THREAD_NAME
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

        return fileCabinetThreads.firstOrNull { 
            it.privateMeta.name == DOCUMENTS_THREAD_NAME
        } ?: throw IllegalStateException("No file cabinet documents thread found")
    }
    
    override fun retrieveFileCabinetImagesStoreId(): String {
        return requireNotNull(retrieveFileCabinetImagesThread().privateMeta.referenceStoreId)
    }
    
    override fun retrieveFileCabinetDocumentsStoreId(): String {
        return requireNotNull(retrieveFileCabinetDocumentsThread().privateMeta.referenceStoreId)
    }

    override suspend fun createDocumentsStoreIfNotExists(): Unit = withContext(Dispatchers.IO) {
        try {
            retrieveFileCabinetDocumentsThread()
        } catch (e: IllegalStateException) {
            val contextId = familyGroupSessionService.getContextId()
            val splitFamilyMembers = FamilyMembersSplitter.split(
                familyGroupService.retrieveFamilyGroupMembersList()
            )

            val users = splitFamilyMembers.members.map { it.toPrivMxUser() }
            val managers = splitFamilyMembers.guardians.map { it.toPrivMxUser() }

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
                name = DOCUMENTS_THREAD_NAME,
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
        createDocumentsStoreIfNotExists()

        val storeId = retrieveFileCabinetDocumentsStoreId()

        val documentMetadata = DocumentMetadata(
            name = documentName,
            mime = documentMimeType
        )
        
        val metadataJson = Json.encodeToString(DocumentMetadata.serializer(), documentMetadata)
        val metadataBytes = metadataJson.encodeToByteArray()
        val metadataLength = metadataBytes.size

        val combinedBytes = ByteArray(4 + metadataLength + documentByteArray.size)

        combinedBytes[0] = (metadataLength shr 24 and 0xFF).toByte()
        combinedBytes[1] = (metadataLength shr 16 and 0xFF).toByte() 
        combinedBytes[2] = (metadataLength shr 8 and 0xFF).toByte()
        combinedBytes[3] = (metadataLength and 0xFF).toByte()

        metadataBytes.copyInto(combinedBytes, 4)

        documentByteArray.copyInto(combinedBytes, 4 + metadataLength)
        
        privMxClient.sendByteArrayToStore(storeId, combinedBytes)
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
        val rawBytes = privMxClient.getFilesAsByteArrayFromStore(actualStoreId, limit, skip)

        return rawBytes.mapIndexed { index, byteArray ->
            try {
                if (byteArray.size >= 4) {
                    val metadataLength = (byteArray[0].toInt() and 0xFF shl 24) or
                                        (byteArray[1].toInt() and 0xFF shl 16) or
                                        (byteArray[2].toInt() and 0xFF shl 8) or
                                        (byteArray[3].toInt() and 0xFF)

                    if (metadataLength > 0 && metadataLength < byteArray.size - 4) {
                        val metadataBytes = byteArray.copyOfRange(4, 4 + metadataLength)
                        val metadataJson = metadataBytes.decodeToString()
                        val contentBytes = byteArray.copyOfRange(4 + metadataLength, byteArray.size)

                        try {
                            val metadata = Json.decodeFromString<DocumentMetadata>(metadataJson)
                            return@mapIndexed DocumentWithMetadata(
                                content = contentBytes,
                                fileName = metadata.name,
                                mimeType = metadata.mime,
                                uploadDate = metadata.timestamp
                            )
                        } catch (e: Exception) {
                            // Fallback to the previous regex approach if JSON parsing fails
                            val nameMatch = Regex(""""name":"([^"]+)"""").find(metadataJson)
                            val mimeMatch = Regex(""""mime":"([^"]+)"""").find(metadataJson)
                            val timestampMatch = Regex(""""timestamp":(\d+)""").find(metadataJson)
                            
                            val fileName = nameMatch?.groupValues?.get(1)
                            val mimeType = mimeMatch?.groupValues?.get(1) 
                            val timestamp = timestampMatch?.groupValues?.get(1)?.toLongOrNull()
                            
                            DocumentWithMetadata(
                                content = contentBytes,
                                fileName = fileName,
                                mimeType = mimeType,
                                uploadDate = timestamp
                            )
                        }
                    }
                }

                val isPdf = FileTypeUtils.isPdfFile(byteArray)
                val fileName = if (isPdf) "Document_${index}.pdf" else "File_${index}.jpg"
                val mimeType = if (isPdf) "application/pdf" else "image/jpeg"
                
                DocumentWithMetadata(
                    content = byteArray,
                    fileName = fileName,
                    mimeType = mimeType,
                    uploadDate = System.currentTimeMillis() - (index * 1000)
                )
            } catch (e: Exception) {
                val isPdf = FileTypeUtils.isPdfFile(byteArray)
                DocumentWithMetadata(
                    content = byteArray,
                    fileName = if (isPdf) "Document_${index}.pdf" else "File_${index}.jpg",
                    mimeType = if (isPdf) "application/pdf" else "image/jpeg",
                    uploadDate = System.currentTimeMillis() - (index * 1000)
                )
            }
        }
    }

    override suspend fun restoreFileCabinetMembership() {
        try {
            val imagesThreadId = retrieveFileCabinetImagesThread().threadId
            val imagesStoreId = retrieveFileCabinetImagesStoreId()
            
            val splitFamilyMembers = FamilyMembersSplitter.split(
                familyGroupService.retrieveFamilyGroupMembersList()
            )

            val users = splitFamilyMembers.members.map { it.toPrivMxUser() }
            val managers = splitFamilyMembers.guardians.map { it.toPrivMxUser() }

            privMxClient.updateThread(imagesThreadId, users, managers)
            privMxClient.updateStore(imagesStoreId, users, managers)

            try {
                val documentsThreadId = retrieveFileCabinetDocumentsThread().threadId
                val documentsStoreId = retrieveFileCabinetDocumentsStoreId()

                privMxClient.updateThread(documentsThreadId, users, managers)
                privMxClient.updateStore(documentsStoreId, users, managers)
            } catch (e: Exception) {
            }
        } catch (e: Exception) {
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