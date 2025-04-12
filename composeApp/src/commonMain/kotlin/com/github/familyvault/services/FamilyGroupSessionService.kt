package com.github.familyvault.services

import com.github.familyvault.backend.PrivMxErrorCodes
import com.github.familyvault.backend.client.IFamilyVaultBackendClient
import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.backend.exceptions.FamilyVaultPrivMxException
import com.github.familyvault.backend.requests.GetMemberFromFamilyGroupRequest
import com.github.familyvault.models.FamilyGroupSession
import com.github.familyvault.models.FamilyMember
import com.github.familyvault.models.PublicEncryptedPrivateKeyPair
import com.github.familyvault.models.PublicPrivateKeyPair
import com.github.familyvault.models.enums.ConnectionStatus

class FamilyGroupSessionService(
    private val privMxClient: IPrivMxClient,
    private val familyVaultBackendClient: IFamilyVaultBackendClient
) : IFamilyGroupSessionService {
    private var session: FamilyGroupSession? = null
    private var currentUser: FamilyMember? = null

    override fun assignSession(
        bridgeUrl: String,
        familyGroupName: String,
        solutionId: String,
        contextId: String,
        keyPair: PublicEncryptedPrivateKeyPair
    ) {
        session = FamilyGroupSession(
            bridgeUrl, familyGroupName, solutionId, contextId, PublicPrivateKeyPair(
                keyPair.publicKey,
                privMxClient.decryptPrivateKeyPassword(keyPair.encryptedPrivateKey)
            )
        )
    }

    override suspend fun connect(): ConnectionStatus {
        requireNotNull(session)
        try {
            privMxClient.establishConnection(
                getBridgeUrl(), getSolutionId(), getPrivateKey()
            )
            currentUser = familyVaultBackendClient.getMemberFromFamilyGroup(
                GetMemberFromFamilyGroupRequest(
                    contextId = getContextId(), userId = null, publicKey = getPublicKey()
                )
            ).member
        } catch (e: FamilyVaultPrivMxException) {
            if (e.errorCode == PrivMxErrorCodes.USER_NOT_IN_CONTEXT) {
                return ConnectionStatus.UserNotFound
            }
        }
        return ConnectionStatus.Success
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

    override fun getCurrentUser(): FamilyMember = requireNotNull(currentUser)

    override fun updateFamilyGroupName(name: String) {
        session = session?.copy(familyGroupName = name)
    }

    override fun getFamilyGroupName(): String {
        requireNotNull(session?.familyGroupName)
        return session!!.familyGroupName
    }

    override fun getPublicKey(): String {
        return requireNotNull(session?.keyPair?.publicKey)
    }
}