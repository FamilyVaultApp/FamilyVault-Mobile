package com.github.familyvault.backend.utils

import com.github.familyvault.backend.models.ThreadPrivateMeta
import com.github.familyvault.backend.models.ThreadPublicMeta
import kotlinx.serialization.json.Json

object ThreadMetaDecoder : IThreadMetaDecoder {
    override fun decodePublicMeta(input: ByteArray): ThreadPublicMeta =
        Json.decodeFromString<ThreadPublicMeta>(input.decodeToString())

    override fun decodePrivateMeta(input: ByteArray): ThreadPrivateMeta =
        Json.decodeFromString<ThreadPrivateMeta>(input.decodeToString())
}