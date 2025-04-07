package com.github.familyvault.utils.mappers

import com.github.familyvault.backend.models.ThreadItem
import com.simplito.java.privmx_endpoint.model.Thread

object PrivMxThreadToThreadItemMapper {
    fun map(thread: Thread): ThreadItem = ThreadItem(
        thread.threadId,
        thread.managers,
        thread.users,
        thread.publicMeta.decodeToString(),
        thread.privateMeta.decodeToString()
    )
}