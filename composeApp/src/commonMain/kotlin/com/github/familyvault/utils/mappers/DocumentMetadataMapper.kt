package com.github.familyvault.utils.mappers

import com.github.familyvault.models.DocumentWithMetadata
import com.github.familyvault.services.IFileOpenerService

object DocumentMetadataMapper {
    fun mapDocumentNames(
        documents: List<DocumentWithMetadata>, 
        fileOpener: IFileOpenerService
    ): List<String> {
        return documents.mapIndexed { index, doc ->
            val fileName = doc.fileName
            val isPdf = fileOpener.isPdfFile(doc.content)
            
            when {
                !fileName.isNullOrBlank() -> fileName
                isPdf -> "Document_${index}.pdf" 
                else -> "File_${index}.jpg"
            }
        }
    }
    
    fun mapDocumentMimeTypes(
        documents: List<DocumentWithMetadata>, 
        fileOpener: IFileOpenerService
    ): List<String> {
        return documents.mapIndexed { index, doc ->
            val mimeType = doc.mimeType
            val isPdf = fileOpener.isPdfFile(doc.content)
            
            when {
                !mimeType.isNullOrBlank() -> mimeType
                isPdf -> "application/pdf"
                else -> "image/jpeg"
            }
        }
    }
}
