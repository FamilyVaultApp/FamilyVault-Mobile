package com.github.familyvault;

import com.github.familyvault.services.IPrivMxEndpointService
import com.github.familyvault.services.createPrivMxService
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module

val sharedModules = module {
    factory {
        createPrivMxService()
    }.bind<IPrivMxEndpointService>()
}

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin{
        config?.invoke(this)
        modules(sharedModules)
    }
}