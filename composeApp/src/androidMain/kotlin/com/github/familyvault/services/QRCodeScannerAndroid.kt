package com.github.familyvault.services

import android.content.Context
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class QRCodeScannerService(private val context: Context): IQRCodeScannerService {

    override suspend fun scanQRCode(): String {
        return suspendCoroutine { continuation ->
            val scannerOptions = GmsBarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build()
            val scanner = GmsBarcodeScanning.getClient(context, scannerOptions)
            scanner.startScan()
                .addOnSuccessListener { barcode ->
                    continuation.resume(barcode.rawValue ?: "No data")
                }
                .addOnCanceledListener {
                    continuation.resume("Canceled")
                }
                .addOnFailureListener { e ->
                    continuation.resume(e.message ?: "Error")
                }
        }
    }
}