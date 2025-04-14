package com.github.familyvault.backend.utils

import com.github.familyvault.backend.models.MessagePrivateMeta
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.protobuf.ProtoBuf

@OptIn(ExperimentalSerializationApi::class)
object MessageMetaDecoder : IMessageMetaDecoder {
    override fun decodePrivateMeta(input: ByteArray): MessagePrivateMeta =
        ProtoBuf.decodeFromByteArray<MessagePrivateMeta>(input)
}