package com.github.familyvault

import com.github.familyvault.repositories.FamilyGroupCredentialsRepository
import com.github.familyvault.repositories.IFamilyGroupCredentialsRepository
import com.github.familyvault.services.ChatService
import com.github.familyvault.services.FamilyGroupService
import com.github.familyvault.services.FamilyGroupSessionService
import com.github.familyvault.services.IChatService
import com.github.familyvault.services.IFamilyGroupService
import com.github.familyvault.services.IFamilyGroupSessionService
import com.github.familyvault.services.IJoinStatusService
import com.github.familyvault.services.JoinStatusService
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module

expect fun getPlatformModules(): Module

val sharedModules = module {
    // Repositories
    single { FamilyGroupCredentialsRepository(get()) }.bind<IFamilyGroupCredentialsRepository>()

    // Services
    single { FamilyGroupSessionService(get()) }.bind<IFamilyGroupSessionService>()
    single {
        FamilyGroupService(
            get(),
            get(),
            get()
        )
    }.bind<IFamilyGroupService>()
    single { JoinStatusService() }.bind<IJoinStatusService>()
    single { ChatService(
        get(),
        get(),
        get()
    )
    }.bind<IChatService>()
}

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            sharedModules,
            getPlatformModules()
        )
    }
}