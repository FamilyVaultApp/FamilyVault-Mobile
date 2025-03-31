package com.github.familyvault.models

import kotlinx.serialization.Serializable

@Serializable
data class PublicPrivateKeyPair(val publicKey: String, val encryptedPrivateKey: String)