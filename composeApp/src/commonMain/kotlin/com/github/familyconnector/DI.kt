package com.github.familyconnector;

import org.koin.core.context.startKoin
import org.koin.dsl.module

val sharedModules = module {
    single {
    }
}

fun initializeDependencyInjection() {
    startKoin{
        modules(sharedModules)
    }
}