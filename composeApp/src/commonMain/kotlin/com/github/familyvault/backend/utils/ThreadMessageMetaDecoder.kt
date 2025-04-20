package com.github.familyvault.backend.utils

import com.github.familyvault.backend.models.ThreadMessagePrivateMeta
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.protobuf.ProtoBuf

@OptIn(ExperimentalSerializationApi::class)
object ThreadMessageMetaDecoder : IThreadMessageMetaDecoder {
    override fun decodePrivateMeta(input: ByteArray): ThreadMessagePrivateMeta =
        ProtoBuf.decodeFromByteArray<ThreadMessagePrivateMeta>(input)
}