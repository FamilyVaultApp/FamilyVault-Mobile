package com.github.familyvault.utils

import io.ktor.util.decodeBase64Bytes
import io.ktor.util.encodeBase64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

object EncryptUtils {
    fun encryptData(data: String, password: String): String {
        val key = generateKeyFromPassword(password)
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val encryptedBytes = cipher.doFinal(data.toByteArray())
        return encryptedBytes.encodeBase64()
    }

    fun decryptData(encryptedData: String, password: String): String {
        val key = generateKeyFromPassword(password)
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, key)
        val decodedBytes = encryptedData.decodeBase64Bytes()
        val decryptedBytes = cipher.doFinal(decodedBytes)
        return String(decryptedBytes)
    }

    private fun generateKeyFromPassword(
        password: String,
        keyLength: Int = 128
    ): SecretKey {
        val key = password.padEnd(keyLength / 8, '0').take(keyLength / 8).toByteArray()
        return SecretKeySpec(key, "AES")
    }
}