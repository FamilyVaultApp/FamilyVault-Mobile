package com.github.familyvault.services

import android.content.Context

fun createQrCodeGenerationService(context: Context): IQRCodeGenerationService {
    return QrCodeGeneratorService(context)
}