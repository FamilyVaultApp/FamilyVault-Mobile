package com.github.familyvault.backend.client

import com.github.familyvault.models.PublicPrivateKeyPair
import com.github.familyvault.services.CurrentSessionContextStore

interface IPrivMxClient {
    fun generatePairOfPrivateAndPublicKey(secret: String, salt: String): PublicPrivateKeyPair
    fun establishConnection()
}

expect fun createPrivMxClient(familyGroupContextService: CurrentSessionContextStore): IPrivMxClient