package com.github.familyvault.services

import com.github.familyvault.models.FamilyMember
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

    suspend fun connect(): ConnectionStatus
    fun updateFamilyGroupName(name: String)
    fun getContextId(): String
    fun getBridgeUrl(): String
    fun getPrivateKey(): String
    fun getSolutionId(): String
    fun getCurrentUser(): FamilyMember
    fun getFamilyGroupName(): String
    fun getPublicKey(): String
}