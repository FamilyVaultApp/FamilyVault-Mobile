package com.github.familyvault.models

data class QrCodeScanResponse(
    val content: String?, val error: String?, val status: QrCodeScanResponseStatus
) {
    companion object {
        fun error(
            error: String?
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