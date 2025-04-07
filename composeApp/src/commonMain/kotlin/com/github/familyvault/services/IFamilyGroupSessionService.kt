package com.github.familyvault.services

import com.github.familyvault.models.PublicEncryptedPrivateKeyPair
import com.github.familyvault.models.enums.ConnectionStatus

interface IFamilyGroupSessionService {
    fun assignSession(
        bridgeUrl: String,
        familyGroupName: String,
        solutionId: String,
        contextId: String,
        keyPair: PublicEncryptedPrivateKeyPair
    )

    fun connect(): ConnectionStatus
    fun getContextId(): String
    fun getBridgeUrl(): String
    fun getPrivateKey(): String
    fun getSolutionId(): String
    fun updateFamilyGroupName(name: String)
    fun getFamilyGroupName() : String
    fun getPublicKey(): String
}