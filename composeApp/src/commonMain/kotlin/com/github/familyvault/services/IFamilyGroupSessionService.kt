package com.github.familyvault.services

import com.github.familyvault.models.PublicPrivateKeyPair

interface IFamilyGroupSessionService {
    fun assignSession(
        bridgeUrl: String,
        familyGroupName: String,
        solutionId: String,
        contextId: String,
        keyPair: PublicPrivateKeyPair,
    )

    fun connect()
    fun getContextId(): String
    fun updateFamilyGroupName(name: String)
    fun getFamilyGroupName() : String
    fun getPublicKey(): String
}