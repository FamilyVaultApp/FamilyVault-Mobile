package com.github.familyvault.services

import com.github.familyvault.models.AddFamilyMemberDataPayload
import com.github.familyvault.models.QrCodeScanResponse

class QrCodeService() : IQRCodeService {
    override suspend fun scanQRCode(): QrCodeScanResponse {
        TODO("Method not yet implemented")
    }

    override suspend fun scanPayload(): AddFamilyMemberDataPayload {
        TODO("Method not yet implemented")
    }
}