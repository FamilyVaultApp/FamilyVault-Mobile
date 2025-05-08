package com.github.familyvault.backend.utils

import com.github.familyvault.backend.models.ThreadMessagePrivateMeta
import kotlinx.serialization.json.Json

object ThreadMessageMetaDecoder : IThreadMessageMetaDecoder {
    override fun decodePrivateMeta(input: ByteArray): ThreadMessagePrivateMeta =
        Json.decodeFromString<ThreadMessagePrivateMeta>(input.decodeToString())
}