package com.github.familyvault;

import com.github.familyvault.services.FamilyGroupContextService
import com.github.familyvault.services.FamilyGroupManagerService
import com.github.familyvault.services.IFamilyGroupContextService
import com.github.familyvault.services.IFamilyGroupManagerService
import com.github.familyvault.services.IPrivMxEndpointService
import com.github.familyvault.services.createPrivMxService
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module

val sharedModules = module {
    single {
        createPrivMxService()
    }.bind<IPrivMxEndpointService>()
    single { FamilyGroupContextService() }.bind<IFamilyGroupContextService>()
    single { FamilyGroupManagerService(get()) }.bind<IFamilyGroupManagerService>()
}

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(sharedModules)
    }
}