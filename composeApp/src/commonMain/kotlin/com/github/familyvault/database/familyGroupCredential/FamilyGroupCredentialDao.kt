package com.github.familyvault.database.familyGroupCredential

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert

@Dao
interface FamilyGroupCredentialDao {
    @Delete
    suspend fun delete(data: FamilyGroupCredential)

    @Query("SELECT * FROM FamilyGroupCredential")
    suspend fun getAll(): List<FamilyGroupCredential>

    @Query("SELECT * FROM FamilyGroupCredential WHERE isDefault = 1 LIMIT 1")
    suspend fun getDefault(): FamilyGroupCredential?

    @Query("SELECT * FROM FamilyGroupCredential WHERE contextId = :contextId AND publicKey = :memberPublicKey")
    suspend fun getByContextIdAndMemberPublicKey(
        contextId: String,
        memberPublicKey: String
    ): FamilyGroupCredential?

    @Transaction
    suspend fun setCredentialAsDefaultAndUnsetOthers(contextId: String, memberPublicKey: String) {
        unsetDefaultForAllCredentials()
        setCredentialAsDefault(contextId, memberPublicKey)
    }

    @Transaction
    suspend fun insertDefaultCredentialAndUnsetOthers(data: FamilyGroupCredential) {
        val dataWithDefaultFlag = data.copy(isDefault = true)
        unsetDefaultForAllCredentials()
        upsert(dataWithDefaultFlag)
    }

    @Query("UPDATE FamilyGroupCredential SET isDefault = 0")
    suspend fun unsetDefaultForAllCredentials()

    @Query("UPDATE FamilyGroupCredential SET isDefault = 1 WHERE contextId = :contextId AND publicKey = :memberPublicKey")
    suspend fun setCredentialAsDefault(contextId: String, memberPublicKey: String)

    @Upsert
    suspend fun upsert(data: FamilyGroupCredential)

    @Query("UPDATE FamilyGroupCredential SET familyGroupName = :familyGroupName WHERE contextId = :contextId")
    suspend fun updateCredentialFamilyGroupName(
        contextId: String,
        familyGroupName: String
    )

    @Query("DELETE FROM FamilyGroupCredential WHERE contextId = :contextId AND publicKey = :memberPublicKey")
    suspend fun deleteCredential(contextId: String, memberPublicKey: String)
}