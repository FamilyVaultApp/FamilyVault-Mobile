package com.github.familyvault.services

import android.content.Context
import com.github.familyvault.exceptions.QrCodeBadScanException
import com.github.familyvault.exceptions.QrCodeCancellationException
import com.github.familyvault.models.AddFamilyMemberDataPayload
import com.github.familyvault.models.QrCodeScanResponse
import com.github.familyvault.models.enums.QrCodeScanResponseStatus
import com.github.familyvault.utils.PayloadDecryptor
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class QrCodeService(private val context: Context) : IQRCodeService {
    override suspend fun scanQRCode(): QrCodeScanResponse {
        return suspendCoroutine { continuation ->
            val scannerOptions =
                GmsBarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build()
            val scanner = GmsBarcodeScanning.getClient(context, scannerOptions)

            scanner.startScan().addOnSuccessListener { barcode ->
                continuation.resume(QrCodeScanResponse.success(barcode.rawValue))
            }.addOnCanceledListener {
                continuation.resume(QrCodeScanResponse.canceled())
            }.addOnFailureListener { e ->
                continuation.resume(QrCodeScanResponse.error(e))
            }
        }
    }

    override suspend fun scanPayload(): AddFamilyMemberDataPayload {
        val scannedResult = scanQRCode()

        if (scannedResult.status == QrCodeScanResponseStatus.CANCELED) {
            throw QrCodeCancellationException()
        }
        if (scannedResult.status == QrCodeScanResponseStatus.ERROR || scannedResult.content == null) {
            throw QrCodeBadScanException(scannedResult.error?.toString())
        }

        return try {
            PayloadDecryptor.decrypt(scannedResult.content)
        } catch (e: Exception) {
            throw QrCodeBadScanException(e.toString())
        }
    }
}