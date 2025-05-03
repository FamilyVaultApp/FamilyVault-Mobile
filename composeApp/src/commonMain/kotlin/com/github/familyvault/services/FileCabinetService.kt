package com.github.familyvault.services

import androidx.compose.runtime.mutableStateListOf
import com.github.familyvault.backend.client.IPrivMxClient

class FileCabinetService(
    private val privMxClient: IPrivMxClient,
    private val imagePickerService: IImagePickerService,
    ) : IFileCabinetService {
    private val storedImageUrls = mutableStateListOf<String>()

    override fun sendImagesToTheFamilyGroupStore (
        threadId: String,
        imageByteArray: ByteArray
    ) {
        val storeId =
            requireNotNull(privMxClient.retrieveThread(threadId).privateMeta.referenceStoreId)

        val rotatedAndCompressedImage =
            imagePickerService.compressAndRotateImage(imageByteArray)
        val fileId = privMxClient.sendByteArrayToStore(storeId, rotatedAndCompressedImage)

        storedImageUrls.add(fileId)
    }
}