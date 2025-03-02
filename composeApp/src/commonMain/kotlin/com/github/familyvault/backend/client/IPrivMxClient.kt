package com.github.familyvault.backend.client

import com.github.familyvault.models.PublicPrivateKeyPair

interface IPrivMxClient {
    fun generatePairOfPrivateAndPublicKey(secret: String, salt: String): PublicPrivateKeyPair
    fun establishConnection(bridgeUrl: String, solutionId: String, privateKey: String)
}