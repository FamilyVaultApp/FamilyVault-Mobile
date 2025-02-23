package com.github.familyvault.services

import com.github.familyvault.exceptions.KeysNotGeneratedException
import com.github.familyvault.models.PublicPrivateKeyPair

class CurrentSessionContextStore : ICurrentSessionContextStore {
    private var contextId: String? = null
    private var solutionId: String? = null
    private var publicPrivateKeyPair: PublicPrivateKeyPair? = null

    override fun setCurrentSolutionId(solutionId: String) {
        this.solutionId = solutionId
    }

    override fun getCurrentSolutionId(): String =
        requireNotNull(solutionId) { "SolutionId is null" }

    override fun setCurrentFamilyGroupId(contextId: String) {
        this.contextId = contextId
    }

    override fun getCurrentFamilyGroupId(): String =
        requireNotNull(contextId) { "ContextId is null" }

    override fun setPairOfKeys(pairOfKeys: PublicPrivateKeyPair) {
        publicPrivateKeyPair = pairOfKeys
    }

    override fun getPublicKey(): String =
        publicPrivateKeyPair?.publicKey
            ?: throw KeysNotGeneratedException("Public key was not generated")

    override fun getPrivateKey(): String =
        publicPrivateKeyPair?.privateKey
            ?: throw KeysNotGeneratedException("Private key was not generated")
}
