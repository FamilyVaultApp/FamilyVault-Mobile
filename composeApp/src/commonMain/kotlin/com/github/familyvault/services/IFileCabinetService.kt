package com.github.familyvault.services

interface IFileCabinetService {
    val storeId: String?

    fun sendImageToFamilyGroupStore (
        threadId: String,
        imageByteArray: ByteArray
    )

    fun getImagesFromFamilyGroupStoreAsByteArray(storeId: String?, limit: Long, skip: Long) : List<ByteArray>
}