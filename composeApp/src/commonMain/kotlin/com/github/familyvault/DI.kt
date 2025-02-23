package com.github.familyvault;

import com.github.familyvault.services.CurrentSessionContextStore
import com.github.familyvault.services.FamilyGroupManagerService
import com.github.familyvault.services.ICurrentSessionContextStore
import com.github.familyvault.services.IFamilyGroupManagerService
import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.backend.client.createPrivMxClient
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module

val sharedModules = module {
    single {
        createPrivMxClient(get())
    }.bind<IPrivMxClient>()
    single { CurrentSessionContextStore() }.bind<ICurrentSessionContextStore>()
    single { FamilyGroupManagerService(get(), get()) }.bind<IFamilyGroupManagerService>()
}

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(sharedModules)
    }
}