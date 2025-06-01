package com.github.familyvault.utils.mappers

import com.github.familyvault.models.fileCabinet.FileCabinetDocument
import kotlinx.serialization.json.Json

object StoreByteArrayToDocumentsFileMapper {
    fun map(byteArray: ByteArray): FileCabinetDocument =
        Json.decodeFromString(byteArray.decodeToString())
}