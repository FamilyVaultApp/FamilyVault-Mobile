package com.github.familyvault.backend.utils

import com.github.familyvault.backend.models.MessagePrivateMeta

interface IMessageMetaEncoder {
    fun encode(privateMeta: MessagePrivateMeta): ByteArray
}