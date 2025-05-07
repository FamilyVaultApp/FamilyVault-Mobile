package com.github.familyvault.backend.utils

import com.github.familyvault.backend.models.StorePublicMeta
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

object StoreMetaDecoder : IStoreMetaDecoder {
    override fun decodePublicMeta(input: ByteArray): StorePublicMeta =
        Json.decodeFromString<StorePublicMeta>(input.decodeToString())
}