package com.github.familyvault.services

interface IFileCabinetService {
    fun sendImagesToTheFamilyGroupStore (
        threadId: String,
        imageByteArray: ByteArray
    )
}