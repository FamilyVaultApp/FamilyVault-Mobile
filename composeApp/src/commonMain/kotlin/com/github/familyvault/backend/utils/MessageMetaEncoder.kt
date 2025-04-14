package com.github.familyvault.backend.utils

import com.github.familyvault.backend.models.MessagePrivateMeta
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf

@OptIn(ExperimentalSerializationApi::class)
object MessageMetaEncoder : IMessageMetaEncoder {
    override fun encode(privateMeta: MessagePrivateMeta): ByteArray =
        ProtoBuf.encodeToByteArray(privateMeta)
}