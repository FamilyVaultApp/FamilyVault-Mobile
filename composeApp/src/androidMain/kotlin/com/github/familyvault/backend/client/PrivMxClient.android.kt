package com.github.familyvault.backend.client

import com.github.familyvault.AppConfig
import com.github.familyvault.models.PublicPrivateKeyPair
import com.github.familyvault.services.CurrentSessionContextStore
import com.simplito.java.privmx_endpoint_extra.lib.PrivmxEndpoint
import com.simplito.java.privmx_endpoint_extra.lib.PrivmxEndpointContainer
import com.simplito.java.privmx_endpoint_extra.model.Modules

private class AndroidPrivMxClient(private val familyGroupContextService: CurrentSessionContextStore) :
    IPrivMxClient {
    private val initModules = setOf(
        Modules.THREAD,
        Modules.STORE,
        Modules.INBOX
    )
    private val container: PrivmxEndpointContainer = PrivmxEndpointContainer()
    private var connection: PrivmxEndpoint? = null

    override fun generatePairOfPrivateAndPublicKey(
        secret: String,
        salt: String
    ): PublicPrivateKeyPair {
        val privateKey = container.cryptoApi.derivePrivateKey(secret, salt)
        val publicKey = container.cryptoApi.derivePublicKey(privateKey)

        return PublicPrivateKeyPair(publicKey, privateKey)
    }

    override fun establishConnection() {
        connection = container.connect(
            initModules,
            familyGroupContextService.getCurrentSolutionId(),
            familyGroupContextService.getPrivateKey(),
            AppConfig.PRIVMX_BRIDGE_URL,
        )
    }
}

actual fun createPrivMxClient(familyGroupContextService: CurrentSessionContextStore): IPrivMxClient =
    AndroidPrivMxClient(familyGroupContextService)