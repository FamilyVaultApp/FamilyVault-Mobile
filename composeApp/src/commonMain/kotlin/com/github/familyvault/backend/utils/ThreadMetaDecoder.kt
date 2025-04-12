package com.github.familyvault.backend.utils

import com.github.familyvault.backend.models.ThreadPrivateMeta
import com.github.familyvault.backend.models.ThreadPublicMeta
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.protobuf.ProtoBuf

@OptIn(ExperimentalSerializationApi::class)
object ThreadMetaDecoder : IThreadMetaDecoder {
    override fun decodePublicMeta(input: ByteArray): ThreadPublicMeta =
        ProtoBuf.decodeFromByteArray<ThreadPublicMeta>(input)

    override fun decodePrivateMeta(input: ByteArray): ThreadPrivateMeta =
        ProtoBuf.decodeFromByteArray<ThreadPrivateMeta>(input)
}