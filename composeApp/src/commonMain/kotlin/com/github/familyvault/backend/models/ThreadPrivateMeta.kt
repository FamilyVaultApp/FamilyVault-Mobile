package com.github.familyvault.backend.models

import com.github.familyvault.models.enums.chat.ThreadIconType
import kotlinx.serialization.Serializable

@Serializable
data class ThreadPrivateMeta(val name: String, val referenceStoreId: String?, val threadIcon: ThreadIconType? = null)