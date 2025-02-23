package com.github.familyvault.backend.client

import com.github.familyvault.services.CurrentSessionContextStore

private class IOSPrivMxClient : IPrivMxClient {
    override fun generatePairOfPrivateAndPublicKey(password: String): String {
        TODO("Not yet implemented")
    }

}

actual fun createPrivMxClient(familyGroupContextService: CurrentSessionContextStore): IPrivMxClient = IOSPrivMxClient();