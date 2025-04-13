package com.github.familyvault.backend.utils

import com.github.familyvault.backend.models.ThreadPrivateMeta
import com.github.familyvault.backend.models.ThreadPublicMeta

interface IThreadMetaEncoder {
    fun encode(publicMeta: ThreadPublicMeta): ByteArray
    fun encode(privateMeta: ThreadPrivateMeta): ByteArray
}