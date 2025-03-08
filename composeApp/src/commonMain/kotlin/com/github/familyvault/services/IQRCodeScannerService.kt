package com.github.familyvault.services

import com.github.familyvault.models.QrCodeScanResponse

interface IQRCodeScannerService {
    suspend fun scanQRCode(): QrCodeScanResponse
}