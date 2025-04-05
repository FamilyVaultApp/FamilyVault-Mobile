package com.github.familyvault.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.familyvault.database.chatMessage.StoredChatMessage
import com.github.familyvault.database.chatMessage.StoredChatMessageDao
import com.github.familyvault.database.familyGroupCredential.FamilyGroupCredential
import com.github.familyvault.database.familyGroupCredential.FamilyGroupCredentialDao

@Database(
    entities = [FamilyGroupCredential::class, StoredChatMessage::class],
    version = 3,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun credentialDao(): FamilyGroupCredentialDao
    abstract fun storedChatMessageDao(): StoredChatMessageDao
}