package com.github.familyvault.utils

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.github.familyvault.AppConfig
import com.github.familyvault.models.AddFamilyMemberDataPayload
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.common.CharacterSetECI
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.util.EnumMap

class QrCodeGenerator : IQrCodeGenerator {
    override fun generate(
        content: AddFamilyMemberDataPayload
    ): ImageBitmap {
        return generateQrCodeBitmap(PayloadEncryptor.encrypt(content))
    }

    override fun generate(content: String): ImageBitmap {
        return generateQrCodeBitmap(content)
    }

    private fun generateQrCodeBitmap(content: String): ImageBitmap {
        val (width, height) = AppConfig.QR_CODE_SIZE
        val qrCodeBitMatrix = MultiFormatWriter().encode(
            content, BarcodeFormat.QR_CODE, width, height, prepareQrHints()
        )

        val pixelArray = generatePixelArrayFromQrCodeBitMatrix(qrCodeBitMatrix, width, height)

        return Bitmap.createBitmap(pixelArray, width, height, Bitmap.Config.ARGB_8888)
            .asImageBitmap()
    }

    private fun prepareQrHints(): EnumMap<EncodeHintType, Any> {
        val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java)
        hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.L
        hints[EncodeHintType.CHARACTER_SET] = CharacterSetECI.UTF8
        return hints
    }

    private fun generatePixelArrayFromQrCodeBitMatrix(
        bitMatrix: BitMatrix, width: Int, height: Int
    ): IntArray {
        val pixels = IntArray(width * height)

        for (y in 0 until height) {
            val offset = y * width

            for (x in 0 until width) {

                pixels[offset + x] = if (bitMatrix.get(x, y)) Color.BLACK
                else Color.WHITE
            }
        }

        return pixels
    }
}