package com.github.familyvault.services

import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.backend.models.PrivMxUser
import com.github.familyvault.backend.models.StoreItem
import com.github.familyvault.models.enums.StoreType
import com.github.familyvault.models.fileCabinet.FileCabinetDocument
import com.github.familyvault.utils.FamilyMembersSplitter
import kotlinx.serialization.json.Json

class FileCabinetService(
    private val privMxClient: IPrivMxClient,
    private val imagePickerService: IImagePickerService,
    private val familyGroupSessionService: IFamilyGroupSessionService,
    private val familyGroupService: IFamilyGroupService
) : IFileCabinetService {
    override suspend fun createInitialStores() {
        val contextId = familyGroupSessionService.getContextId()
        val splitFamilyMembers = FamilyMembersSplitter.split(
            familyGroupService.retrieveFamilyGroupMembersList()
        )

        val users = splitFamilyMembers.members.map { it.toPrivMxUser() }
        val managers = splitFamilyMembers.guardians.map { it.toPrivMxUser() }

        createFileCabinetGalleryStore(contextId, users, managers)
        createFileCabinetDocumentsStore(contextId, users, managers)
    }

    override fun sendImageToFileCabinetGallery(
        image: ByteArray
    ) {
        val store = retrieveFileCabinetGalleryStore()
        val rotatedAndCompressedImage = imagePickerService.compressAndRotateImage(image)
        privMxClient.sendByteArrayToStore(store.id, rotatedAndCompressedImage)
    }

    override fun getImagesFromFileCabinetGallery(): List<ByteArray> {
        val store = retrieveFileCabinetGalleryStore()
        return privMxClient.getFilesAsByteArrayFromStore(store.id, 100, 0)
    }

    override suspend fun sendDocumentToFileCabinetDocuments(
        content: ByteArray, name: String, mimeType: String
    ) {
        val store = retrieveFileCabinetDocumentsStore()

        val document = FileCabinetDocument(
            name, mimeType, content
        )
        val serializedDocument = Json.encodeToString(document)

        privMxClient.sendByteArrayToStore(store.id, serializedDocument.encodeToByteArray())
    }

    override fun getDocumentsFromFileCabinetDocuments(): List<FileCabinetDocument> {
        val store = retrieveFileCabinetDocumentsStore()

        return privMxClient.getFilesAsByteArrayFromStore(store.id, 100, 0).map {
            Json.decodeFromString<FileCabinetDocument>(it.decodeToString())
        }
    }

    override suspend fun restoreFileCabinetMembership() {
        val galleryStore = retrieveFileCabinetGalleryStore()
        val documentsStore = retrieveFileCabinetDocumentsStore()

        val splitFamilyMembers = FamilyMembersSplitter.split(
            familyGroupService.retrieveFamilyGroupMembersList()
        )

        val users = splitFamilyMembers.members.map { it.toPrivMxUser() }
        val managers = splitFamilyMembers.guardians.map { it.toPrivMxUser() }

        privMxClient.updateStore(galleryStore.id, users, managers)
        privMxClient.updateStore(documentsStore.id, users, managers)
    }

    override fun getGalleryStoreId(): String = retrieveFileCabinetGalleryStore().id

    private fun createFileCabinetGalleryStore(
        contextId: String, users: List<PrivMxUser>, managers: List<PrivMxUser>
    ) {
        privMxClient.createStore(
            contextId, users, managers, StoreType.FILE_CABINET_IMAGES.toString()
        )
    }

    private fun createFileCabinetDocumentsStore(
        contextId: String, users: List<PrivMxUser>, managers: List<PrivMxUser>
    ) {
        privMxClient.createStore(
            contextId, users, managers, StoreType.FILE_CABINET_DOCUMENTS.toString()
        )
    }

    private fun retrieveFileCabinetGalleryStore(): StoreItem {
        val contextId = familyGroupSessionService.getContextId()
        return privMxClient.retrieveAllStoresWithType(
            contextId, StoreType.FILE_CABINET_IMAGES.toString(), 0, 100
        ).first()
    }

    private fun retrieveFileCabinetDocumentsStore(): StoreItem {
        val contextId = familyGroupSessionService.getContextId()
        return privMxClient.retrieveAllStoresWithType(
            contextId, StoreType.FILE_CABINET_DOCUMENTS.toString(), 0, 100
        ).first()
    }

}