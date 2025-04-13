package com.github.familyvault.backend.utils

import com.github.familyvault.backend.models.ThreadPublicMeta
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.protobuf.ProtoBuf

@OptIn(ExperimentalSerializationApi::class)
object ThreadMessageDecoder : IThreadMessageDecoder {
    override fun decodeMessage(input: ByteArray): ThreadPublicMeta =
        ProtoBuf.decodeFromByteArray<ThreadPublicMeta>(input)
}