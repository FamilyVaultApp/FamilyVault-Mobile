package com.github.familyvault.services

import com.github.familyvault.backend.models.ThreadItem

interface IFileCabinetService {
    suspend fun createInitialStores()
    fun retrieveFileCabinetThread(): ThreadItem
    fun retrieveFileCabinetStoreId(): String

    fun sendImageToFamilyGroupStore (
        imageByteArray: ByteArray
    )

    fun getImagesFromFamilyGroupStoreAsByteArray(storeId: String?, limit: Long, skip: Long) : List<ByteArray>
    suspend fun restoreFileCabinetMembership()
}