package com.github.familyvault.services

import android.content.Context

fun createQrCodeService(context: Context): IQRCodeService {
    return QRCodeService(context)
}