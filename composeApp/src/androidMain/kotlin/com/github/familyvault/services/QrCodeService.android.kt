package com.github.familyvault.services

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.github.familyvault.models.AddFamilyMemberDataPayload
import com.github.familyvault.models.QrCodeScanResponse
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.common.CharacterSetECI
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import kotlinx.serialization.json.Json
import java.util.EnumMap
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class QRCodeService(private val context: Context) : IQRCodeService {

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
                    continuation.resume(QrCodeScanResponse.error(e.message))
                }
        }
    }

    override suspend fun scanPayload(): AddFamilyMemberDataPayload {
        val scannedResult = scanQRCode()

        if (scannedResult.error != null || scannedResult.content == null) {
            throw Exception(scannedResult.error)
        }

        return Json.decodeFromString(scannedResult.content)
    }

    override fun generateQRCode(qrCodeContent: String): ImageBitmap? {
        val bitMatrix: BitMatrix
        try {
            val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java)
            hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
            hints[EncodeHintType.CHARACTER_SET] = CharacterSetECI.UTF8

            bitMatrix = MultiFormatWriter().encode(
                qrCodeContent, BarcodeFormat.QR_CODE, 500, 500, hints
            )
        } catch (exception: IllegalArgumentException) {
            return null
        }

        val bitMatrixWidth = bitMatrix.width

        val bitMatrixHeight = bitMatrix.height

        val pixels = IntArray(bitMatrixWidth * bitMatrixHeight)

        for (y in 0 until bitMatrixHeight) {
            val offset = y * bitMatrixWidth

            for (x in 0 until bitMatrixWidth) {

                pixels[offset + x] = if (bitMatrix.get(x, y)) Color.BLACK
                else Color.WHITE
            }
        }

        val imageBitmap: ImageBitmap =
            Bitmap.createBitmap(pixels, bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_8888)
                .asImageBitmap()
        return imageBitmap
    }
}