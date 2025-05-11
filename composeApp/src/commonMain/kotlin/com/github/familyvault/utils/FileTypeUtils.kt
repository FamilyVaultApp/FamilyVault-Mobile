package com.github.familyvault.utils

object FileTypeUtils {
    
    fun isPdfFile(fileBytes: ByteArray): Boolean {
        return fileBytes.size >= 4 && 
               fileBytes[0].toInt() == 0x25 &&
               fileBytes[1].toInt() == 0x50 &&
               fileBytes[2].toInt() == 0x44 &&
               fileBytes[3].toInt() == 0x46
    }
    
    fun detectMimeType(fileBytes: ByteArray): String {
        return if (isPdfFile(fileBytes)) {
            "application/pdf"
        } else {
            "image/jpeg"
        }
    }
    
    fun getSuggestedExtension(fileBytes: ByteArray): String {
        return if (isPdfFile(fileBytes)) {
            ".pdf"
        } else {
            ".jpg"
        }
    }
}
