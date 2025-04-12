package com.github.familyvault

import com.github.familyvault.backend.client.FamilyVaultBackendClient
import com.github.familyvault.backend.client.IFamilyVaultBackendClient
import com.github.familyvault.repositories.FamilyGroupCredentialsRepository
import com.github.familyvault.repositories.IFamilyGroupCredentialsRepository
import com.github.familyvault.repositories.IStoredChatMessageRepository
import com.github.familyvault.repositories.StoredChatMessageRepository
import com.github.familyvault.services.ChatListenerService
import com.github.familyvault.services.ChatService
import com.github.familyvault.services.FamilyGroupService
import com.github.familyvault.services.FamilyGroupSessionService
import com.github.familyvault.services.IChatListenerService
import com.github.familyvault.services.IChatService
import com.github.familyvault.services.IFamilyGroupService
import com.github.familyvault.services.IFamilyGroupSessionService
import com.github.familyvault.services.IJoinStatusService
import com.github.familyvault.services.IFamilyMemberPermissionGroupService
import com.github.familyvault.services.JoinStatusService
import com.github.familyvault.services.FamilyMemberPermissionGroupService
import com.github.familyvault.states.CurrentChatState
import com.github.familyvault.states.ICurrentChatState
import com.github.familyvault.states.IJoinFamilyGroupPayloadState
import com.github.familyvault.states.JoinFamilyGroupPayloadState
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module

expect fun getPlatformModules(): Module

val sharedModules = module {
    // Repositories
    single { FamilyGroupCredentialsRepository(get()) }.bind<IFamilyGroupCredentialsRepository>()
    single { StoredChatMessageRepository(get()) }.bind<IStoredChatMessageRepository>()

    // Services
    single { FamilyGroupSessionService(get(), get()) }.bind<IFamilyGroupSessionService>()
    single {
        FamilyGroupService(
            get(),
            get(),
            get(),
            get()
        )
    }.bind<IFamilyGroupService>()
    single { JoinStatusService() }.bind<IJoinStatusService>()
    single {
        ChatService(
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }.bind<IChatService>()
    single {
        ChatListenerService(get(), get(), get(), get())
    }.bind<IChatListenerService>()

    // Backend client
    single { FamilyVaultBackendClient() }.bind<IFamilyVaultBackendClient>()

    // States
    single { JoinFamilyGroupPayloadState() }.bind<IJoinFamilyGroupPayloadState>()
    single { CurrentChatState(get()) }.bind<ICurrentChatState>()
    single { FamilyMemberPermissionGroupService(get()) }.bind<IFamilyMemberPermissionGroupService>()
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