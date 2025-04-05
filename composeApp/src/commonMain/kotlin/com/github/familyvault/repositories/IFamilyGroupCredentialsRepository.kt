package com.github.familyvault.repositories

import com.github.familyvault.database.familyGroupCredential.FamilyGroupCredential
import com.github.familyvault.models.PublicEncryptedPrivateKeyPair

interface IFamilyGroupCredentialsRepository {
    suspend fun addDefaultCredential(
        name: String,
        solutionId: String,
        contextId: String,
        keyPairs: PublicEncryptedPrivateKeyPair,
        encryptedPrivateKeyPassword: String
    )

    suspend fun setDefaultCredentialByContextId(contextId: String)

    suspend fun getDefaultCredential() : FamilyGroupCredential?
}