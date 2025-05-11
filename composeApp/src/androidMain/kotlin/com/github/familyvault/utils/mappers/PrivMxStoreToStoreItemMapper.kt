package com.github.familyvault.utils.mappers

import com.github.familyvault.backend.models.StoreItem
import com.github.familyvault.backend.utils.StoreMetaDecoder
import com.simplito.java.privmx_endpoint.model.Store

object PrivMxStoreToStoreItemMapper {
    fun map(store: Store): StoreItem = StoreItem(
        id = store.storeId,
        managers = store.managers,
        users = store.users,
        publicMeta = StoreMetaDecoder.decodePublicMeta(store.publicMeta)
    )
}