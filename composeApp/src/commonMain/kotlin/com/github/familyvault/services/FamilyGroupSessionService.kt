package com.github.familyvault.services

import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.models.FamilyGroupSession
import com.github.familyvault.models.PublicPrivateKeyPair

class FamilyGroupSessionService(
    private val privMxClient: IPrivMxClient
) :
    IFamilyGroupSessionService {
    private var session: FamilyGroupSession? = null

    override fun assignSession(
        bridgeUrl: String,
        familyGroupName: String,
        solutionId: String,
        contextId: String,
        keyPair: PublicPrivateKeyPair
    ) {
        session = FamilyGroupSession(
            bridgeUrl, familyGroupName, solutionId, contextId, keyPair
        )
    }

    override fun connect() {
        requireNotNull(session)

        privMxClient.establishConnection(
            session!!.bridgeUrl,
            session!!.solutionId,
            session!!.keyPair.privateKey,
        )
    }

    override fun getContextId(): String {
        requireNotNull(session?.contextId)
        return session!!.contextId
    }

    override fun updateFamilyGroupName(name: String) {
        session = session?.copy(familyGroupName = name)
    }

    override fun getFamilyGroupName(): String {
        requireNotNull(session?.familyGroupName)
        return session!!.familyGroupName
    }
}