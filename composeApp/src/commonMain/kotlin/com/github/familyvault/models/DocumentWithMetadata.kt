package com.github.familyvault.models

data class DocumentWithMetadata(
    val content: ByteArray,
    val fileName: String? = null,
    val mimeType: String? = null,
    val uploadDate: Long? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as DocumentWithMetadata

        if (!content.contentEquals(other.content)) return false
        if (fileName != other.fileName) return false
        if (mimeType != other.mimeType) return false
        if (uploadDate != other.uploadDate) return false

        return true
    }

    override fun hashCode(): Int {
        var result = content.contentHashCode()
        result = 31 * result + (fileName?.hashCode() ?: 0)
        result = 31 * result + (mimeType?.hashCode() ?: 0)
        result = 31 * result + (uploadDate?.hashCode() ?: 0)
        return result
    }
}
