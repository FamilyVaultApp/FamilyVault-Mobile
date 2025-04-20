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

    @Query("SELECT * FROM FamilyGroupCredential WHERE contextId = :contextId")
    suspend fun getByContextId(contextId: String): FamilyGroupCredential?

    @Transaction
    suspend fun setCredentialAsDefaultByContextIdAndUnsetOthers(contextId: String) {
        unsetDefaultForAllCredentials()
        setCredentialAsDefaultByContextID(contextId)
    }

    @Transaction
    suspend fun insertDefaultCredentialAndUnsetOthers(data: FamilyGroupCredential) {
        val dataWithDefaultFlag = data.copy(isDefault = true)
        unsetDefaultForAllCredentials()
        upsert(dataWithDefaultFlag)
    }

    @Query("UPDATE FamilyGroupCredential SET isDefault = 0")
    suspend fun unsetDefaultForAllCredentials()

    @Query("UPDATE FamilyGroupCredential SET isDefault = 1 WHERE contextId = :contextId")
    suspend fun setCredentialAsDefaultByContextID(contextId: String)

    @Upsert
    suspend fun upsert(data: FamilyGroupCredential)

    @Query("UPDATE FamilyGroupCredential SET name = :name WHERE contextId = :contextId")
    suspend fun updateCredentialName(contextId: String, name: String)

    @Query("DELETE FROM FamilyGroupCredential WHERE contextId = :contextId")
    suspend fun deleteCredential(contextId: String)
}