package com.github.familyvault.qrcodescanner

expect class QRCodeScanner {
    suspend fun scanQRCode(): String
}