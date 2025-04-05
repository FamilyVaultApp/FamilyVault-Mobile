package com.github.familyvault.services

import com.github.familyvault.models.PublicEncryptedPrivateKeyPair

interface IFamilyGroupSessionService {
    fun assignSession(
        bridgeUrl: String,
        solutionId: String,
        contextId: String,
        keyPair: PublicEncryptedPrivateKeyPair
    )

    fun connect()
    fun getContextId(): String
    fun getPrivateKey(): String
    fun getBridgeUrl(): String
    fun getSolutionId(): String
}