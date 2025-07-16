package com.github.familyvault.services

import com.github.familyvault.models.AddFamilyMemberDataPayload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


class NfcService() : INfcService {

    override val tags: Flow<AddFamilyMemberDataPayload> = callbackFlow {
    }
    private fun isResponseOkay(response: ByteArray): Boolean {
        if (response.size < 2) return false
        val sw1 = response[response.size - 2]
        val sw2 = response[response.size - 1]
        return sw1 == 0x90.toByte() && sw2 == 0x00.toByte()
    }

    override suspend fun registerApp() {
        TODO("Method not yet implemented")
    }

    override fun unregisterApp() {
        TODO("Method not yet implemented")
    }

    override suspend fun setEmulateMode(data: AddFamilyMemberDataPayload) {
        TODO("Method not yet implemented")
    }

    override suspend fun setReadMode() {
        TODO("Method not yet implemented")
    }

}
