package com.github.familyvault.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.familyvault.database.familyGroupCredential.FamilyGroupCredential
import com.github.familyvault.database.familyGroupCredential.FamilyGroupCredentialDao

@Database(
    entities = [FamilyGroupCredential::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun credentialDao(): FamilyGroupCredentialDao
}