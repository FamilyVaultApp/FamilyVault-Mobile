package com.github.familyvault.backend.utils

import com.github.familyvault.backend.models.ThreadMessagePrivateMeta

interface IThreadMessageEncoder {
    fun encode(privateMeta: ThreadMessagePrivateMeta): ByteArray
}