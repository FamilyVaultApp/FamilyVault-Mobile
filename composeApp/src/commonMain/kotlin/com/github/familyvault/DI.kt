package com.github.familyvault

import com.github.familyvault.backend.client.FamilyVaultBackendClient
import com.github.familyvault.backend.client.IFamilyVaultBackendClient
import com.github.familyvault.repositories.FamilyGroupCredentialsRepository
import com.github.familyvault.repositories.IFamilyGroupCredentialsRepository
import com.github.familyvault.repositories.IStoredChatMessageRepository
import com.github.familyvault.repositories.StoredChatMessageRepository
import com.github.familyvault.services.ChatService
import com.github.familyvault.services.FamilyGroupService
import com.github.familyvault.services.FamilyGroupSessionService
import com.github.familyvault.services.FamilyMemberAdditionService
import com.github.familyvault.services.FamilyMemberPermissionGroupService
import com.github.familyvault.services.FileCabinetService
import com.github.familyvault.services.IChatService
import com.github.familyvault.services.IFamilyGroupService
import com.github.familyvault.services.IFamilyGroupSessionService
import com.github.familyvault.services.IFamilyMemberAdditionService
import com.github.familyvault.services.IFamilyMemberPermissionGroupService
import com.github.familyvault.services.IFileCabinetService
import com.github.familyvault.services.IJoinStatusService
import com.github.familyvault.services.ISavedFamilyGroupsService
import com.github.familyvault.services.ITaskService
import com.github.familyvault.services.JoinStatusService
import com.github.familyvault.services.SavedFamilyGroupsService
import com.github.familyvault.services.TaskService
import com.github.familyvault.services.listeners.ChatMessagesListenerService
import com.github.familyvault.services.listeners.ChatThreadListenerService
import com.github.familyvault.services.listeners.FileCabinetListenerService
import com.github.familyvault.services.listeners.IChatMessagesListenerService
import com.github.familyvault.services.listeners.IChatThreadListenerService
import com.github.familyvault.services.listeners.IFileCabinetListenerService
import com.github.familyvault.services.listeners.ITaskListenerService
import com.github.familyvault.services.listeners.ITaskListListenerService
import com.github.familyvault.services.listeners.TaskListenerService
import com.github.familyvault.services.listeners.TaskListListenerService
import com.github.familyvault.states.ChatImagesState
import com.github.familyvault.states.CurrentChatState
import com.github.familyvault.states.CurrentChatThreadsState
import com.github.familyvault.states.CurrentDraftFamilyGroupState
import com.github.familyvault.states.CurrentDraftFamilyMemberState
import com.github.familyvault.states.CurrentEditChatState
import com.github.familyvault.states.FamilyMembersState
import com.github.familyvault.states.IChatImagesState
import com.github.familyvault.states.ICurrentChatState
import com.github.familyvault.states.ICurrentChatThreadsState
import com.github.familyvault.states.ICurrentDraftFamilyGroupState
import com.github.familyvault.states.ICurrentDraftFamilyMemberState
import com.github.familyvault.states.ICurrentEditChatState
import com.github.familyvault.states.IFamilyMembersState
import com.github.familyvault.states.IJoinFamilyGroupPayloadState
import com.github.familyvault.states.ISelfHostedAddressState
import com.github.familyvault.states.ITaskListState
import com.github.familyvault.states.JoinFamilyGroupPayloadState
import com.github.familyvault.states.SelfHostedAddressState
import com.github.familyvault.states.TaskListState
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
            get(),
            get(),
        )
    }.bind<IFamilyGroupService>()
    single { JoinStatusService(get()) }.bind<IJoinStatusService>()
    single {
        ChatService(
            get(),
            get(),
            get(),
            get(),
            get(),
        )
    }.bind<IChatService>()
    single {
        ChatMessagesListenerService(get(), get(), get(), get())
    }.bind<IChatMessagesListenerService>()
    single { ChatThreadListenerService(get()) }.bind<IChatThreadListenerService>()
    single { TaskListListenerService(get()) }.bind<ITaskListListenerService>()
    single { TaskListenerService(get()) }.bind<ITaskListenerService>()
    single {
        FamilyMemberAdditionService(
            get(), get(), get(), get(), get()
        )
    }.bind<IFamilyMemberAdditionService>()
    single {
        FamilyMemberPermissionGroupService(
            get(), get()
        )
    }.bind<IFamilyMemberPermissionGroupService>()
    single {
        SavedFamilyGroupsService(get())
    }.bind<ISavedFamilyGroupsService>()
    single {
        TaskService(get(), get(), get())
    }.bind<ITaskService>()
    single {
        FileCabinetService(get(), get(), get(), get())
    }.bind<IFileCabinetService>()
    single {
        FileCabinetListenerService(get())
    }.bind<IFileCabinetListenerService>()

    // Backend client
    single { FamilyVaultBackendClient() }.bind<IFamilyVaultBackendClient>()

    // States
    single { JoinFamilyGroupPayloadState() }.bind<IJoinFamilyGroupPayloadState>()
    single { CurrentChatState(get()) }.bind<ICurrentChatState>()
    single { CurrentChatThreadsState() }.bind<ICurrentChatThreadsState>()
    single { ChatImagesState() }.bind<IChatImagesState>()
    single { TaskListState(get()) }.bind<ITaskListState>()
    single { FamilyMembersState(get()) }.bind<IFamilyMembersState>()
    single { CurrentEditChatState() }.bind<ICurrentEditChatState>()
    single { CurrentDraftFamilyMemberState() }.bind<ICurrentDraftFamilyMemberState>()
    single { CurrentDraftFamilyGroupState() }.bind<ICurrentDraftFamilyGroupState>()
    single { SelfHostedAddressState() }.bind<ISelfHostedAddressState>()
}


fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            sharedModules, getPlatformModules()
        )
    }
}