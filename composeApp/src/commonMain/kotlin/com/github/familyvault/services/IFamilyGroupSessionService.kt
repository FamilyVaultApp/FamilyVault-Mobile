package com.github.familyvault.services

import com.github.familyvault.models.PublicPrivateKeyPair

interface IFamilyGroupSessionService {
    fun assignSession(
        bridgeUrl: String,
        solutionId: String,
        contextId: String,
        keyPair: PublicPrivateKeyPair,
        familyGroupName: String
    )

    fun connect()
    fun getContextId(): String
    fun getFamilyGroupName(): String
    fun updateFamilyGroupName(newName: String)
}