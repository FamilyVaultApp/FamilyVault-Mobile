package com.github.familyvault.backend.models

import kotlinx.serialization.Serializable

@Serializable
data class ThreadPrivateMeta(val name: String, val referenceStoreId: String?, val managersPublicKeys: List<String>?)