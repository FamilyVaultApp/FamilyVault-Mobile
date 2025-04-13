package com.github.familyvault.backend.utils

import com.github.familyvault.backend.models.ThreadPrivateMeta
import com.github.familyvault.backend.models.ThreadPublicMeta

interface IThreadMetaDecoder {
    fun decodePublicMeta(input: ByteArray): ThreadPublicMeta
    fun decodePrivateMeta(input: ByteArray): ThreadPrivateMeta
}