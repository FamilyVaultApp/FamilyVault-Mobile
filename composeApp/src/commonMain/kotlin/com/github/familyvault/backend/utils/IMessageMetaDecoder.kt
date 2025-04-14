package com.github.familyvault.backend.utils

import com.github.familyvault.backend.models.MessagePrivateMeta

interface IMessageMetaDecoder {
    fun decodePrivateMeta(input: ByteArray): MessagePrivateMeta
}