package com.github.familyvault.services

import com.github.familyvault.AppConfig
import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.backend.models.ThreadItem

class FileCabinetService(
    private val privMxClient: IPrivMxClient,
    private val imagePickerService: IImagePickerService,
    private val familyGroupSessionService: IFamilyGroupSessionService
) : IFileCabinetService {
    override fun retrieveFileCabinetThread(): ThreadItem {
        val contextId = familyGroupSessionService.getContextId()
        val fileCabinetThreadsId = privMxClient.retrieveAllThreadsWithTag(
            contextId,
            AppConfig.FILE_CABINET_THREAD_TAG,
            startIndex = 0,
            pageSize = 100
        )
        return fileCabinetThreadsId.first()
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
}