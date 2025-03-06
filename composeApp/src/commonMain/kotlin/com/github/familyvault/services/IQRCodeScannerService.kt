package com.github.familyvault.services

interface IQRCodeScannerService {
    suspend fun scanQRCode(): String
}