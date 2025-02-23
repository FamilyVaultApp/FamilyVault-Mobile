package com.github.familyvault.backend.client

import com.github.familyvault.models.PublicPrivateKeyPair

private class IOSPrivMxClient : IPrivMxClient {
    override fun generatePairOfPrivateAndPublicKey(
        secret: String,
        salt: String
    ): PublicPrivateKeyPair {
        TODO("Not yet implemented")
    }

    override fun establishConnection(bridgeUrl: String, solutionId: String, privateKey: String) {
        TODO("Not yet implemented")
    }
}

actual fun createPrivMxClient(): IPrivMxClient = IOSPrivMxClient()