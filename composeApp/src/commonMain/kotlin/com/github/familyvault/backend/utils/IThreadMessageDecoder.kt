package com.github.familyvault.backend.utils

import com.github.familyvault.backend.models.ThreadPublicMeta

interface IThreadMessageDecoder {
    fun decodeMessage(input: ByteArray): ThreadPublicMeta
}