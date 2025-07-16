package com.github.familyvault.utils

import androidx.compose.ui.graphics.ImageBitmap
import com.github.familyvault.models.AddFamilyMemberDataPayload

class QrCodeGenerator : IQrCodeGenerator {
    override fun generate(
        content: AddFamilyMemberDataPayload
    ): ImageBitmap {
        TODO("Method not yet implemented")
    }

    override fun generate(content: String): ImageBitmap {
        TODO("Method not yet implemented")
    }

    private fun generateQrCodeBitmap(content: String): ImageBitmap {
        TODO("Method not yet implemented")
    }
}