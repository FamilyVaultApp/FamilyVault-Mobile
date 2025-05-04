package com.github.familyvault.backend.utils

import com.github.familyvault.backend.models.ThreadMessagePrivateMeta
import io.ktor.utils.io.core.toByteArray
import kotlinx.serialization.json.Json

object ThreadMessageEncoder : IThreadMessageEncoder {
    override fun encode(privateMeta: ThreadMessagePrivateMeta): ByteArray =
        Json.encodeToString(privateMeta).toByteArray()
}