package com.github.familyvault.services

import android.content.Context

fun createQrCodeScannerService(context: Context): IQRCodeScannerService {
    return QRCodeScannerService(context)
}