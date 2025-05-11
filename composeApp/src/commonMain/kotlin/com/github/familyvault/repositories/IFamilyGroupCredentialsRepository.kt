package com.github.familyvault.repositories

import com.github.familyvault.database.familyGroupCredential.FamilyGroupCredential
import com.github.familyvault.models.PublicEncryptedPrivateKeyPair
import com.github.familyvault.models.SelfHostedConnectionInfo

interface IFamilyGroupCredentialsRepository {
    suspend fun addDefaultCredential(
        name: String,
        solutionId: String,
        contextId: String,
        keyPairs: PublicEncryptedPrivateKeyPair,
        encryptedPrivateKeyPassword: String,
        connectionInfo: SelfHostedConnectionInfo? = null
    )

    suspend fun setDefaultCredentialByContextId(contextId: String)

    suspend fun getDefaultCredential(): FamilyGroupCredential?

    suspend fun updateCredentialFamilyGroupName(contextId: String, name: String)

    suspend fun deleteCredential(contextId: String)

    suspend fun getAllCredentials(): List<FamilyGroupCredential>

    suspend fun getCredentialByContextId(contextId: String): FamilyGroupCredential
}