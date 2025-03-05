package com.github.familyvault.qrcodescanner

import androidx.compose.runtime.Composable

interface IQRCodeScanner {
    @Composable
    fun ScanQRCode(): String
}