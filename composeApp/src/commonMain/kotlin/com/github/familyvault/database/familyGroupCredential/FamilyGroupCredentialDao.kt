package com.github.familyvault.database.familyGroupCredential

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface FamilyGroupCredentialDao {
    @Upsert
    suspend fun upsert(data: FamilyGroupCredential)

    @Delete
    suspend fun delete(data: FamilyGroupCredential)

    @Query("SELECT * FROM FamilyGroupCredential")
    suspend fun getAll(): List<FamilyGroupCredential>

    @Query("SELECT * FROM FamilyGroupCredential WHERE isDefault = 1 LIMIT 1")
    suspend fun getDefault(): FamilyGroupCredential?

    @Query("UPDATE FamilyGroupCredential SET isDefault = 0")
    suspend fun unsetDefaultForAllCredentials()
}