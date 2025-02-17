package com.github.familyvault.services

import com.simplito.java.privmx_endpoint_extra.lib.PrivmxEndpointContainer

private class AndroidPrivMxService : IPrivMxEndpointService {
    private val container: PrivmxEndpointContainer = PrivmxEndpointContainer()

    override fun generatePrivateKey(password: String): String {
        return container.cryptoApi.derivePrivateKey(password, "FamilyVault")
    }
}

actual fun createPrivMxService(): IPrivMxEndpointService = AndroidPrivMxService()