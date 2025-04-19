package com.github.familyvault.backend.utils

import com.github.familyvault.backend.models.StorePublicMeta

interface IStoreMetaEncoder {
    fun encode(publicMeta: StorePublicMeta): ByteArray
}