package com.github.familyvault.services

import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.models.FamilyGroupSession
import com.github.familyvault.models.PublicPrivateKeyPair

class FamilyGroupSessionService(
    private val privMxClient: IPrivMxClient
) : IFamilyGroupSessionService {
    private var session: FamilyGroupSession? = null

    override fun assignSession(
        bridgeUrl: String, solutionId: String, contextId: String, keyPair: PublicPrivateKeyPair
    ) {
        session = FamilyGroupSession(
            bridgeUrl, solutionId, contextId, keyPair
        )
    }

    override fun connect() {
        requireNotNull(session)

        privMxClient.establishConnection(
            session!!.bridgeUrl, session!!.solutionId, getDecryptedPrivateKey()
        )
    }

    override fun getContextId(): String {
        return requireNotNull(session?.contextId) { "contextId can't be null" }
    }

    override fun getDecryptedPrivateKey(): String {
        val encryptedPrivateKey =
            requireNotNull(session?.keyPair?.encryptedPrivateKey) { "privateKey can't be null" }
        return privMxClient.decryptPrivateKeyPassword(encryptedPrivateKey)
    }
}