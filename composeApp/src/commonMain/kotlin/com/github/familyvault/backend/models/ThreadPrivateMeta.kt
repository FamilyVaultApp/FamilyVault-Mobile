package com.github.familyvault.backend.models

import com.github.familyvault.models.enums.chat.ChatIconType
import kotlinx.serialization.Serializable

@Serializable
data class ThreadPrivateMeta(val name: String, val referenceStoreId: String?, val threadIcon: ChatIconType? = null)