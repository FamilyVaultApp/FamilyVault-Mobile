package com.github.familyvault.services

import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.models.FamilyGroupSession
import com.github.familyvault.models.PublicPrivateKeyPair

class FamilyGroupSessionService(private val privMxClient: IPrivMxClient) :
    IFamilyGroupSessionService {
    private var session: FamilyGroupSession? = null

    override fun connect() {
        requireNotNull(session)

        privMxClient.establishConnection(
            session!!.bridgeUrl,
            session!!.solutionId,
            session!!.keyPair.privateKey
        )
    }

    override fun assignNewSession(newSession: FamilyGroupSession) {
        session = newSession
    }

    override fun assignNewSession(
        bridgeUrl: String,
        solutionId: String,
        contextId: String,
        keyPair: PublicPrivateKeyPair
    ) {
        session = FamilyGroupSession(
            bridgeUrl, solutionId, contextId, keyPair
        )
    }

    override fun getPrivateKey(): String = session!!.keyPair.privateKey

    override fun getPublicKey(): String = session!!.keyPair.publicKey

    override fun getContextId(): String = session!!.contextId
}