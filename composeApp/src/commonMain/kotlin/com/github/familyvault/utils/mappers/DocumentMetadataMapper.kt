package com.github.familyvault.utils.mappers

import com.github.familyvault.models.DocumentWithMetadata
import com.github.familyvault.utils.FileTypeUtils

object DocumentMetadataMapper {
    fun mapDocumentNames(
        documents: List<DocumentWithMetadata>
    ): List<String> {
        return documents.mapIndexed { index, doc ->
            val fileName = doc.fileName
            val isPdf = FileTypeUtils.isPdfFile(doc.content)
            
            when {
                !fileName.isNullOrBlank() -> fileName
                isPdf -> "Document_${index}.pdf" 
                else -> "File_${index}.jpg"
            }
        }
    }
    
    fun mapDocumentMimeTypes(
        documents: List<DocumentWithMetadata>
    ): List<String> {
        return documents.mapIndexed { _, doc ->
            val mimeType = doc.mimeType
            val isPdf = FileTypeUtils.isPdfFile(doc.content)
            
            when {
                !mimeType.isNullOrBlank() -> mimeType
                isPdf -> "application/pdf"
                else -> "image/jpeg"
            }
        }
    }
}
