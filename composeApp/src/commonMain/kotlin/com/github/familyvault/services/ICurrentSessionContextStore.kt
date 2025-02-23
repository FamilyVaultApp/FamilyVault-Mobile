package com.github.familyvault.services

import com.github.familyvault.models.PublicPrivateKeyPair

interface ICurrentSessionContextStore {
    fun setCurrentSolutionId(solutionId: String)
    fun getCurrentSolutionId(): String
    fun setCurrentFamilyGroupId(contextId: String)
    fun getCurrentFamilyGroupId(): String
    fun setPairOfKeys(pairOfKeys: PublicPrivateKeyPair)
    fun getPublicKey(): String
    fun getPrivateKey(): String
}