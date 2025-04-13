package com.github.familyvault.backend.utils

import com.github.familyvault.backend.models.StorePublicMeta
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf

@OptIn(ExperimentalSerializationApi::class)
object StoreMetaEncoder : IStoreMetaEncoder {
    override fun encode(publicMeta: StorePublicMeta): ByteArray =
        ProtoBuf.encodeToByteArray(publicMeta)
}