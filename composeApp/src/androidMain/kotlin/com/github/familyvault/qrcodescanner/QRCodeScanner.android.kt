package com.github.familyvault.qrcodescanner

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

internal class QRCodeScanner(): IQRCodeScanner {

    @Composable
    override fun ScanQRCode(): String {
        val context = LocalContext.current
        var rawValue by remember { mutableStateOf("") }

        LaunchedEffect(Unit) {
            val scanner = GmsBarcodeScanning.getClient(context)
            val scannerOptions = GmsBarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build()

            scanner.startScan()
                .addOnSuccessListener { barcode ->
                    rawValue = barcode.rawValue ?: "No data"
                }
                .addOnCanceledListener {
                    rawValue = "Canceled"
                }
                .addOnFailureListener { e ->
                    rawValue = e.message ?: "Error"
                }
        }

        return rawValue
    }

}