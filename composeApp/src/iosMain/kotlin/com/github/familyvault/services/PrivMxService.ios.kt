package com.github.familyvault.services

private class IOSPrivMxService : IPrivMxEndpointService {
    override fun generatePrivateKey(password: String): String {
        TODO("Not yet implemented")
    }

}

actual fun createPrivMxService(): IPrivMxEndpointService = IOSPrivMxService();