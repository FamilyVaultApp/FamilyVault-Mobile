package com.github.familyvault.backend.utils

import com.github.familyvault.backend.models.ThreadMessagePrivateMeta

interface IThreadMessageMetaDecoder {
    fun decodePrivateMeta(input: ByteArray): ThreadMessagePrivateMeta
}