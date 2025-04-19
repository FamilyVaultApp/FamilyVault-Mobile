package com.github.familyvault.backend.models

import kotlinx.datetime.LocalDateTime

data class ThreadMessageItem(
    val messageId: String,
    val messageContent: String?,
    val authorId: String,
    val authorPublicKey: String,
    val createDate: LocalDateTime,
    val privateMeta: ThreadMessagePrivateMeta
)