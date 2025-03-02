package com.github.familyvault.backend.client

import com.github.familyvault.models.PublicPrivateKeyPair
import com.simplito.java.privmx_endpoint_extra.lib.PrivmxEndpoint
import com.simplito.java.privmx_endpoint_extra.lib.PrivmxEndpointContainer
import com.simplito.java.privmx_endpoint_extra.model.Modules

internal class AndroidPrivMxClient(certsPath: String) :
    IPrivMxClient {
    private val initModules = setOf(
        Modules.THREAD,
        Modules.STORE,
        Modules.INBOX
    )
    private val container: PrivmxEndpointContainer = PrivmxEndpointContainer().also {
        it.setCertsPath(certsPath)
    }
    private var connection: PrivmxEndpoint? = null

    override fun generatePairOfPrivateAndPublicKey(
        secret: String,
        salt: String
    ): PublicPrivateKeyPair {
        val privateKey = container.cryptoApi.derivePrivateKey(secret, salt)
        val publicKey = container.cryptoApi.derivePublicKey(privateKey)

        return PublicPrivateKeyPair(publicKey, privateKey)
    }

    override fun establishConnection(bridgeUrl: String, solutionId: String, privateKey: String) {
        connection = container.connect(
            initModules,
            privateKey,
            solutionId,
            bridgeUrl
        )
    }
}