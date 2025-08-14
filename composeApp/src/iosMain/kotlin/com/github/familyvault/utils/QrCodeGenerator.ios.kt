package com.github.familyvault.utils

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.github.familyvault.models.AddFamilyMemberDataPayload
import io.ktor.utils.io.core.toByteArray
import kotlinx.cinterop.ExperimentalForeignApi
import org.jetbrains.skia.Image
import org.jetbrains.skia.makeFromEncoded
import platform.CoreGraphics.CGAffineTransformMakeScale
import platform.CoreImage.CIContext
import platform.CoreImage.CIFilter
import platform.CoreImage.PNGRepresentationOfImage
import platform.CoreImage.QRCodeGenerator
import platform.CoreImage.kCIFormatARGB8
import platform.Foundation.setValue
import kotlin.collections.emptyMap

class QrCodeGenerator : IQrCodeGenerator {
    override fun generate(
        content: AddFamilyMemberDataPayload
    ): ImageBitmap {
        return generateQrCodeBitmap(PayloadEncryptor.encrypt(content))
    }

    override fun generate(content: String): ImageBitmap {
        return generateQrCodeBitmap(content)
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun generateQrCodeBitmap(content: String): ImageBitmap {
        val filter = CIFilter.QRCodeGenerator().apply {
            setValue(content.toByteArray().asData(), forKey= "inputMessage")
            setValue("L", forKey= "inputCorrectionLevel")
        }
        val context = CIContext()
        val data = filter.outputImage?.let { outputImage ->
            val scaledImage = outputImage.imageByApplyingTransform(CGAffineTransformMakeScale(5.0,5.0))
            context.PNGRepresentationOfImage(scaledImage, format = kCIFormatARGB8, colorSpace = outputImage.colorSpace, options = emptyMap<Any?,Any>())
        }
        return Image.makeFromEncoded(data!!).toComposeImageBitmap()
    }
}