package com.github.familyvault.models

import kotlinx.serialization.Serializable

@Serializable
data class PublicEncryptedPrivateKeyPair(val publicKey: String, val encryptedPrivateKey: String)