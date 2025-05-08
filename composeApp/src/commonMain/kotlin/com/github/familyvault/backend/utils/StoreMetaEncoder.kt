package com.github.familyvault.backend.utils

import com.github.familyvault.backend.models.StorePublicMeta
import io.ktor.utils.io.core.toByteArray
import kotlinx.serialization.json.Json

object StoreMetaEncoder : IStoreMetaEncoder {
    override fun encode(publicMeta: StorePublicMeta): ByteArray =
        Json.encodeToString(publicMeta).toByteArray()
}