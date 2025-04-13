package com.github.familyvault.backend.utils

import com.github.familyvault.backend.models.ThreadPrivateMeta
import com.github.familyvault.backend.models.ThreadPublicMeta
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf

@OptIn(ExperimentalSerializationApi::class)
object ThreadMetaEncoder : IThreadMetaEncoder {
    override fun encode(publicMeta: ThreadPublicMeta): ByteArray =
        ProtoBuf.encodeToByteArray(publicMeta)

    override fun encode(privateMeta: ThreadPrivateMeta): ByteArray =
        ProtoBuf.encodeToByteArray(privateMeta)
}