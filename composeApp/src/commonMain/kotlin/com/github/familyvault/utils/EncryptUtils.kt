package com.github.familyvault.utils

import com.github.familyvault.backend.client.PrivMxClient
import io.ktor.util.decodeBase64Bytes
import io.ktor.util.encodeBase64
import io.ktor.utils.io.core.toByteArray

object EncryptUtils {
    fun PrivMxClient.encryptData(data: String, password: String): String {
        return cryptoApi.encryptDataSymmetric(data.toByteArray(),generateKeyFromPassword(password)).encodeBase64()
    }

    fun PrivMxClient.decryptData(encryptedData: String, password: String): String {
        return cryptoApi.decryptDataSymmetric(encryptedData.decodeBase64Bytes(),generateKeyFromPassword(password)).decodeToString()
    }

    private fun generateKeyFromPassword(
        password: String,
        keyLength: Int = 256
    ): ByteArray {
        return password.padEnd(keyLength / 8, '0').take(keyLength / 8).toByteArray()
    }
}