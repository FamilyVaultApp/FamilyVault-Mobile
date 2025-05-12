package com.github.familyvault.models

import kotlinx.serialization.Serializable

@Serializable
data class DocumentMetadata(
    val name: String,
    val mime: String,
    val timestamp: Long = System.currentTimeMillis()
)
