package com.github.familyvault.repositories

import com.github.familyvault.database.familyGroupCredential.FamilyGroupCredential
import com.github.familyvault.models.PublicEncryptedPrivateKeyPair

interface IFamilyGroupCredentialsRepository {
    suspend fun addDefaultCredential(
        name: String,
        solutionId: String,
        contextId: String,
        keyPairs: PublicEncryptedPrivateKeyPair,
        encryptedPrivateKeyPassword: String,
        firstname: String,
        lastname: String?
    )

    suspend fun setDefaultCredential(contextId: String, memberPublicKey: String)

    suspend fun getDefaultCredential(): FamilyGroupCredential?

    suspend fun updateCredentialFamilyGroupName(
        contextId: String, name: String
    )

    suspend fun deleteCredential(contextId: String, memberPublicKey: String)

    suspend fun getAllCredentials(): List<FamilyGroupCredential>

    suspend fun getCredential(
        contextId: String,
        memberPublicKey: String,
    ): FamilyGroupCredential
}