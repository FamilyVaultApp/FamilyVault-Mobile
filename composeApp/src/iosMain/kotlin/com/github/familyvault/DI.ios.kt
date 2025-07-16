package com.github.familyvault

import com.github.familyvault.database.AppDatabase
import com.github.familyvault.database.getAppDatabase
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun getPlatformModules(): Module = module{
    single { getAppDatabase(get()) }.bind<AppDatabase>()
}