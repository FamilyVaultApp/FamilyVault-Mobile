package com.github.familyvault

import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.backend.client.createPrivMxClient
import com.github.familyvault.services.FamilyGroupManagerService
import com.github.familyvault.services.FamilyGroupSessionService
import com.github.familyvault.services.IFamilyGroupManagerService
import com.github.familyvault.services.IFamilyGroupSessionService
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module

val sharedModules = module {
    single {
        createPrivMxClient()
    }.bind<IPrivMxClient>()
    single { FamilyGroupSessionService(get()) }.bind<IFamilyGroupSessionService>()
    single { FamilyGroupManagerService(get(), get()) }.bind<IFamilyGroupManagerService>()
}

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(sharedModules)
    }
}