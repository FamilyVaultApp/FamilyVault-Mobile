package com.github.familyvault.utils

import androidx.compose.ui.graphics.ImageBitmap
import com.github.familyvault.models.AddFamilyMemberDataPayload

interface IQrCodeGenerator {
    fun generate(
        content: AddFamilyMemberDataPayload,
    ): ImageBitmap

    fun generate(
        content: String,
    ): ImageBitmap
}