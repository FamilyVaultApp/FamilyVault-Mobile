package com.github.familyvault.backend.client

import com.github.familyvault.models.PublicPrivateKeyPair

interface IPrivMxClient {
    fun generatePairOfPrivateAndPublicKey(password: String): PublicPrivateKeyPair
    fun encryptPrivateKeyPassword(password: String): String
    fun decryptPrivateKeyPassword(encryptedPassword: String): String
    fun establishConnection(bridgeUrl: String, solutionId: String, privateKey: String)
}