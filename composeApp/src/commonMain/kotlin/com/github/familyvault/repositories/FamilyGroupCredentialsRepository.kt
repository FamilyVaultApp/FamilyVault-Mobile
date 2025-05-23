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
        firstname: String,
        lastname: String
    ) {
        val credentialDao = appDatabase.credentialDao()
        credentialDao.insertDefaultCredentialAndUnsetOthers(
            FamilyGroupCredential(
                familyGroupName = name,
                solutionId = solutionId,
                contextId = contextId,
                encryptedPrivateKey = keyPairs.encryptedPrivateKey,
                encryptedPrivateKeyPassword = encryptedPrivateKeyPassword,
                publicKey = keyPairs.publicKey,
                firstname = firstname,
                lastname = lastname
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
        credentialDao.updateCredentialFamilyGroupName(contextId, name)
    }

    override suspend fun deleteCredential(contextId: String) {
        val credentialDao = appDatabase.credentialDao()
        val credential = requireNotNull(credentialDao.getByContextId(contextId))
        credentialDao.deleteCredential(contextId)

        if (!credential.isDefault) {
            return
        }

        val credentials = credentialDao.getAll()

        if (credentials.isEmpty()) {
            return
        }

        credentialDao.setCredentialAsDefaultByContextID(credentials.first().contextId)
    }

    override suspend fun getAllCredentials(): List<FamilyGroupCredential> {
        val credentialDao = appDatabase.credentialDao()
        return credentialDao.getAll()
    }

    override suspend fun getCredentialByContextId(contextId: String): FamilyGroupCredential {
        val credentialDao = appDatabase.credentialDao()
        return requireNotNull(credentialDao.getByContextId(contextId))
    }
}