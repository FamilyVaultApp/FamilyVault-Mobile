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
        lastname: String?,
        backendUrl: String?
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
                lastname = lastname,
                backendUrl = backendUrl
            )
        )
    }

    override suspend fun setDefaultCredential(
        contextId: String,
        memberPublicKey: String
    ) {
        val credentialDao = appDatabase.credentialDao()
        credentialDao.setCredentialAsDefaultAndUnsetOthers(contextId, memberPublicKey)
    }

    override suspend fun getDefaultCredential(): FamilyGroupCredential? {
        val credentialDao = appDatabase.credentialDao()
        return credentialDao.getDefault()
    }

    override suspend fun updateCredentialFamilyGroupName(
        contextId: String,
        name: String
    ) {
        val credentialDao = appDatabase.credentialDao()
        credentialDao.updateCredentialFamilyGroupName(contextId, name)
    }

    override suspend fun deleteCredential(contextId: String, memberPublicKey: String) {
        val credentialDao = appDatabase.credentialDao()
        val credential = requireNotNull(
            credentialDao.getByContextIdAndMemberPublicKey(
                contextId,
                memberPublicKey
            )
        )
        credentialDao.deleteCredential(contextId, memberPublicKey)

        if (!credential.isDefault) {
            return
        }

        val credentials = credentialDao.getAll()

        if (credentials.isEmpty()) {
            return
        }

        val correctCredential = credentials.first()
        credentialDao.setCredentialAsDefault(
            correctCredential.contextId,
            correctCredential.publicKey
        )
    }

    override suspend fun getAllCredentials(): List<FamilyGroupCredential> {
        val credentialDao = appDatabase.credentialDao()
        return credentialDao.getAll()
    }

    override suspend fun getCredential(
        contextId: String,
        memberPublicKey: String
    ): FamilyGroupCredential {
        val credentialDao = appDatabase.credentialDao()
        return requireNotNull(
            credentialDao.getByContextIdAndMemberPublicKey(
                contextId,
                memberPublicKey
            )
        )
    }
}