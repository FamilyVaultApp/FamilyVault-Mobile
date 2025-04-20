package com.github.familyvault.backend.utils

import com.github.familyvault.backend.models.ThreadMessagePrivateMeta
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf

@OptIn(ExperimentalSerializationApi::class)
object ThreadMessageEncoder : IThreadMessageEncoder {
    override fun encode(privateMeta: ThreadMessagePrivateMeta): ByteArray =
        ProtoBuf.encodeToByteArray(privateMeta)
}