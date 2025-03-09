package com.github.familyvault.services

import androidx.compose.ui.graphics.ImageBitmap

interface IQRCodeGenerationService {
    fun generateQRCode(qrCodeContent: String): ImageBitmap?
}