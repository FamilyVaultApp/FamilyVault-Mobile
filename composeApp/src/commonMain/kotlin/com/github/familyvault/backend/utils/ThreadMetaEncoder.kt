package com.github.familyvault.backend.utils

import com.github.familyvault.backend.models.ThreadPrivateMeta
import com.github.familyvault.backend.models.ThreadPublicMeta
import io.ktor.utils.io.core.toByteArray
import kotlinx.serialization.json.Json

object ThreadMetaEncoder : IThreadMetaEncoder {
    override fun encode(publicMeta: ThreadPublicMeta): ByteArray =
        Json.encodeToString(publicMeta).toByteArray()

    override fun encode(privateMeta: ThreadPrivateMeta): ByteArray =
        Json.encodeToString(privateMeta).toByteArray()
}