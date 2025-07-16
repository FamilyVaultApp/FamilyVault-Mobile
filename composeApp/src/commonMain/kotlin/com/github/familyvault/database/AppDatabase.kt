package com.github.familyvault.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.github.familyvault.database.chatMessage.StoredChatMessage
import com.github.familyvault.database.chatMessage.StoredChatMessageDao
import com.github.familyvault.database.familyGroupCredential.FamilyGroupCredential
import com.github.familyvault.database.familyGroupCredential.FamilyGroupCredentialDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(
    entities = [FamilyGroupCredential::class, StoredChatMessage::class],
    version = 6,
)
@ConstructedBy(AppDatabaseDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun credentialDao(): FamilyGroupCredentialDao
    abstract fun storedChatMessageDao(): StoredChatMessageDao
}

fun getAppDatabase(builder: RoomDatabase.Builder<AppDatabase>): AppDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
