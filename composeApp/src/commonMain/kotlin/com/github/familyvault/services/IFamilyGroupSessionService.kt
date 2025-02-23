package com.github.familyvault.services

import com.github.familyvault.models.FamilyGroupSession
import com.github.familyvault.models.PublicPrivateKeyPair

interface IFamilyGroupSessionService {
    fun connect()
    fun assignNewSession(newSession: FamilyGroupSession)
    fun assignNewSession(
        bridgeUrl: String,
        solutionId: String,
        contextId: String,
        keyPair: PublicPrivateKeyPair
    )
    fun getPrivateKey(): String
    fun getPublicKey(): String
    fun getContextId(): String
}