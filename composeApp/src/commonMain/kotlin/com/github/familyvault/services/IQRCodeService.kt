package com.github.familyvault.services

import com.github.familyvault.models.AddFamilyMemberDataPayload
import com.github.familyvault.models.QrCodeScanResponse

interface IQRCodeService {
    suspend fun scanQRCode(): QrCodeScanResponse
    suspend fun scanPayload(): AddFamilyMemberDataPayload
}