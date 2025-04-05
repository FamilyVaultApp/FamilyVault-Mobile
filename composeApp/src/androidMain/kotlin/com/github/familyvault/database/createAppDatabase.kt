package com.github.familyvault.database

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers

fun createAppDatabase(context: Context): AppDatabase {
    val dbFile = context.getDatabasePath("people.db")
    return Room.databaseBuilder<AppDatabase>(
        context = context.applicationContext,
        name = dbFile.absolutePath
    )
        .setDriver(BundledSQLiteDriver())
        // TODO: Usunięcie destruktywnej migracji przy wersji 1.0
        .fallbackToDestructiveMigration(false)
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}