package com.github.familyvault.services

import com.github.familyvault.models.PublicPrivateKeyPair

interface IFamilyGroupSessionService {
    fun assignSession(
        bridgeUrl: String,
        solutionId: String,
        contextId: String,
        keyPair: PublicPrivateKeyPair
    )

    fun connect()
    fun getContextId(): String
}