package com.github.familyvault.backend.utils

import com.github.familyvault.backend.models.StorePublicMeta
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.protobuf.ProtoBuf

@OptIn(ExperimentalSerializationApi::class)
object StoreMetaDecoder : IStoreMetaDecoder {
    override fun decodePublicMeta(input: ByteArray): StorePublicMeta =
        ProtoBuf.decodeFromByteArray<StorePublicMeta>(input)
}