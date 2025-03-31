package com.github.familyvault.services

import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.models.FamilyGroupSession
import com.github.familyvault.models.PublicEncryptedPrivateKeyPair
import com.github.familyvault.models.PublicPrivateKeyPair

class FamilyGroupSessionService(
    private val privMxClient: IPrivMxClient
) : IFamilyGroupSessionService {
    private var session: FamilyGroupSession? = null

    override fun assignSession(
        bridgeUrl: String,
        solutionId: String,
        contextId: String,
        keyPair: PublicEncryptedPrivateKeyPair
    ) {
        session = FamilyGroupSession(
            bridgeUrl, solutionId, contextId, PublicPrivateKeyPair(
                keyPair.publicKey,
                privMxClient.decryptPrivateKeyPassword(keyPair.encryptedPrivateKey)
            )
        )
    }

    override fun connect() {
        requireNotNull(session)

        privMxClient.establishConnection(
            getBridgeUrl(), getSolutionId(), getPrivateKey()
        )
    }

    override fun getContextId(): String {
        return requireNotNull(session?.contextId)
    }

    override fun getPrivateKey(): String {
        return requireNotNull(session?.keyPair?.privateKey)
    }

    override fun getBridgeUrl(): String {
        return requireNotNull(session?.bridgeUrl)
    }

    override fun getSolutionId(): String {
        return requireNotNull(session?.solutionId)
    }
}