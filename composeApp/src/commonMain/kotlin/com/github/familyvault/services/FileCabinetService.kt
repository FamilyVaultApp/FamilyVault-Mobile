package com.github.familyvault.services

import androidx.compose.runtime.mutableStateListOf
import com.github.familyvault.backend.client.IPrivMxClient

class FileCabinetService(
    private val privMxClient: IPrivMxClient,
    private val imagePickerService: IImagePickerService,
    ) : IFileCabinetService {

    override fun sendImageToFamilyGroupStore(
        threadId: String,
        imageByteArray: ByteArray
    ) {
        val storeId =
            requireNotNull(privMxClient.retrieveThread(threadId).privateMeta.referenceStoreId)

        val rotatedAndCompressedImage =
            imagePickerService.compressAndRotateImage(imageByteArray)

        privMxClient.sendByteArrayToStore(storeId, rotatedAndCompressedImage)
    }

    override fun getImagesFromFamilyGroupStoreAsByteArray(storeId: String, limit: Long, skip: Long) : List<ByteArray> {
        return privMxClient.getFilesAsByteArrayFromStore(storeId, limit, skip)
    }
}