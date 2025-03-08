package com.github.familyvault.services

import android.content.Context
import com.github.familyvault.models.QrCodeScanResponse
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class QRCodeScannerService(private val context: Context) : IQRCodeScannerService {

    override suspend fun scanQRCode(): QrCodeScanResponse {
        return suspendCoroutine { continuation ->
            val scannerOptions = GmsBarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build()
            val scanner = GmsBarcodeScanning.getClient(context, scannerOptions)

            scanner.startScan()
                .addOnSuccessListener { barcode ->
                    continuation.resume(QrCodeScanResponse.success(barcode.rawValue))
                }
                .addOnCanceledListener {
                    continuation.resume(QrCodeScanResponse.canceled())
                }
                .addOnFailureListener { e ->
                    continuation.resume(QrCodeScanResponse.error(e.message))
                }
        }
    }
}