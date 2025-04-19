package com.github.familyvault.backend.utils

import com.github.familyvault.backend.models.StorePublicMeta

interface IStoreMetaDecoder {
    fun decodePublicMeta(input: ByteArray): StorePublicMeta
}