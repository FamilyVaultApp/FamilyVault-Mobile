package com.github.familyvault.services

import com.github.familyvault.AppConfig
import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.backend.models.ThreadItem
import com.github.familyvault.models.enums.StoreType
import com.github.familyvault.models.enums.fileCabinet.FileCabinetThreadType
import com.github.familyvault.utils.FamilyMembersSplitter

class FileCabinetService(
    private val privMxClient: IPrivMxClient,
    private val imagePickerService: IImagePickerService,
    private val familyGroupSessionService: IFamilyGroupSessionService,
    private val familyGroupService: IFamilyGroupService
) : IFileCabinetService {
    override suspend fun createInitialStores() {
        val contextId = familyGroupSessionService.getContextId()
        val familyGroupName = familyGroupSessionService.getFamilyGroupName()
        val splitFamilyMembers = FamilyMembersSplitter.split(
            familyGroupService.retrieveFamilyGroupMembersList()
        )

        val users = splitFamilyMembers.members.map { it.toPrivMxUser() }
        val managers = splitFamilyMembers.guardians.map { it.toPrivMxUser() }

        val storeId = privMxClient.createStore(
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
            name = familyGroupName,
            referenceStoreId = storeId,
            threadInitialCreators = emptyList()
        )
    }

    override fun retrieveFileCabinetThread(): ThreadItem {
        val contextId = familyGroupSessionService.getContextId()
        val fileCabinetThreadsId = privMxClient.retrieveAllThreadsWithTag(
            contextId,
            AppConfig.FILE_CABINET_THREAD_TAG,
            startIndex = 0,
            pageSize = 100
        )

        return fileCabinetThreadsId.firstOrNull()
            ?: throw IllegalStateException("No file cabinet thread found")
    }

    override fun retrieveFileCabinetStoreId(): String {
        return requireNotNull(retrieveFileCabinetThread().privateMeta.referenceStoreId)
    }

    override fun sendImageToFamilyGroupStore(
        imageByteArray: ByteArray
    ) {
        val threadId = retrieveFileCabinetThread().threadId

        val storeId =
            requireNotNull(privMxClient.retrieveThread(threadId).privateMeta.referenceStoreId)

        val rotatedAndCompressedImage =
            imagePickerService.compressAndRotateImage(imageByteArray)

        privMxClient.sendByteArrayToStore(storeId, rotatedAndCompressedImage)
    }

    override fun getImagesFromFamilyGroupStoreAsByteArray(
        storeId: String?,
        limit: Long,
        skip: Long
    ): List<ByteArray> {
        return privMxClient.getFilesAsByteArrayFromStore(storeId, limit, skip)
    }

    override fun sendDocumentToFamilyGroupStore(
        documentByteArray: ByteArray,
        documentName: String,
        documentMimeType: String
    ) {
        val threadId = retrieveFileCabinetThread().threadId

        val storeId =
            requireNotNull(privMxClient.retrieveThread(threadId).privateMeta.referenceStoreId)

        privMxClient.sendByteArrayToStore(storeId, documentByteArray)
    }

    override fun getDocumentsFromFamilyGroupStore(
        storeId: String?,
        limit: Long,
        skip: Long
    ): List<ByteArray> {
        return privMxClient.getFilesAsByteArrayFromStore(storeId, limit, skip)
    }

    override suspend fun restoreFileCabinetMembership() {
        val threadId = retrieveFileCabinetThread().threadId
        val storeId = retrieveFileCabinetStoreId()
        val splitFamilyMembers = FamilyMembersSplitter.split(
            familyGroupService.retrieveFamilyGroupMembersList()
        )

        val users = splitFamilyMembers.members.map { it.toPrivMxUser() }
        val managers = splitFamilyMembers.guardians.map { it.toPrivMxUser() }

        privMxClient.updateThread(
            threadId,
            users,
            managers
        )

        privMxClient.updateStore(
            storeId,
            users,
            managers
        )
    }
}