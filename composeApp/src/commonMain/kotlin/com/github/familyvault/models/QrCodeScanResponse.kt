package com.github.familyvault.models

import com.github.familyvault.models.enums.QrCodeScanResponseStatus

data class QrCodeScanResponse(
    val content: String?, val error: Throwable?, val status: QrCodeScanResponseStatus
) {
    companion object {
        fun error(
            error: Throwable?
        ): QrCodeScanResponse {
            return QrCodeScanResponse(null, error, QrCodeScanResponseStatus.ERROR)
        }

        fun success(
            content: String?
        ): QrCodeScanResponse {
            return QrCodeScanResponse(content, null, QrCodeScanResponseStatus.SUCCESS)
        }

        fun canceled(): QrCodeScanResponse {
            return QrCodeScanResponse(null, null, QrCodeScanResponseStatus.CANCELED)
        }
    }
}