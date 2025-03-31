package com.github.familyvault.repositories

import com.github.familyvault.database.AppDatabase
import com.github.familyvault.database.familyGroupCredential.FamilyGroupCredential
import com.github.familyvault.models.PublicPrivateKeyPair

class FamilyGroupCredentialsRepository(private val appDatabase: AppDatabase) :
    IFamilyGroupCredentialsRepository {
    override suspend fun addDefaultCredential(
        name: String,
        solutionId: String,
        contextId: String,
        keyPairs: PublicPrivateKeyPair,
        encryptedPrivateKeyPassword: String,
    ) {
        val credentialDao = appDatabase.credentialDao()
        credentialDao.insertDefaultCredentialAndUnsetOthers(
            FamilyGroupCredential(
                name = name,
                solutionId = solutionId,
                contextId = contextId,
                encryptedPrivateKey = keyPairs.encryptedPrivateKey,
                encryptedPrivateKeyPassword = encryptedPrivateKeyPassword,
                publicKey = keyPairs.publicKey
            )
        )
    }

    override suspend fun setDefaultCredentialByContextId(contextId: String) {
        val credentialDao = appDatabase.credentialDao()
        credentialDao.setCredentialAsDefaultByContextIdAndUnsetOthers(contextId)
    }

    override suspend fun getDefaultCredential(): FamilyGroupCredential? {
        val credentialDao = appDatabase.credentialDao()
        return credentialDao.getDefault()
    }
}