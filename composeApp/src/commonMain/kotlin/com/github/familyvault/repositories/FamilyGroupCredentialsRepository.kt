package com.github.familyvault.repositories

import com.github.familyvault.database.AppDatabase
import com.github.familyvault.database.familyGroupCredential.FamilyGroupCredential
import com.github.familyvault.models.PublicEncryptedPrivateKeyPair

class FamilyGroupCredentialsRepository(private val appDatabase: AppDatabase) :
    IFamilyGroupCredentialsRepository {
    override suspend fun addDefaultCredential(
        name: String,
        solutionId: String,
        contextId: String,
        keyPairs: PublicEncryptedPrivateKeyPair,
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

    override suspend fun updateCredentialFamilyGroupName(contextId: String, name: String) {
        val credentialDao = appDatabase.credentialDao()
        credentialDao.updateCredentialName(contextId, name)
    }

    override suspend fun deleteCredential(contextId: String) {
        val credentialDao = appDatabase.credentialDao()
        credentialDao.deleteCredential(contextId)
    }
}