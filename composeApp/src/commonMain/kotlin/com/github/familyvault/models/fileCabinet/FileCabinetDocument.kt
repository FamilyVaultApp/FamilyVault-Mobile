package com.github.familyvault.models.fileCabinet

import kotlinx.serialization.Serializable

@Serializable
data class FileCabinetDocument(
    val name: String,
    val mimeType: String,
    val content: ByteArray,
    val contentPreview: ByteArray? = null
)

fun FileCabinetDocument.isPdf() = mimeType == "application/pdf"

fun FileCabinetDocument.isImage(): Boolean {
    return mimeType in setOf(
        "image/png",
        "image/jpeg",
        "image/jpg",
    )
}