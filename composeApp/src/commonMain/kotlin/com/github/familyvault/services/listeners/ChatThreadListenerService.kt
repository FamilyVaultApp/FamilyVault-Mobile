package com.github.familyvault.services.listeners

import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.models.chat.ChatThread
import com.github.familyvault.models.enums.chat.ChatThreadType

class ChatThreadListenerService(
    private val privMxClient: IPrivMxClient,
) : IChatThreadListenerService {
    companion object {
        const val CREATE_EVENT_NAME = "CHAT_THREAD_CREATE"
        const val UPDATE_EVENT_NAME = "CHAT_THREAD_UPDATE"
    }

    override fun startListeningForNewChatThread(onNewChatThread: (ChatThread) -> Unit) {
        privMxClient.registerOnThreadCreated(CREATE_EVENT_NAME) {
            onNewChatThread(
                ChatThread(
                    id = it.threadId,
                    name = it.privateMeta.name,
                    participantsIds = it.users,
                    lastMessage = null,
                    type = ChatThreadType.valueOf(it.publicMeta.type),
                    referenceStoreId = it.privateMeta.referenceStoreId,
                    iconType = it.privateMeta.threadIcon
                )
            )
        }
    }

    override fun startListeningForUpdatedChatThread(onUpdatedChatThread: (ChatThread) -> Unit) {
        privMxClient.registerOnThreadUpdated(UPDATE_EVENT_NAME) {
            onUpdatedChatThread(
                ChatThread(
                    id = it.threadId,
                    name = it.privateMeta.name,
                    participantsIds = it.users,
                    lastMessage = null,
                    type = ChatThreadType.valueOf(it.publicMeta.type),
                    referenceStoreId = it.privateMeta.referenceStoreId,
                    iconType = it.privateMeta.threadIcon
                )
            )
        }
    }

    override fun unregisterAllListeners() {
        privMxClient.unregisterAllEvents(CREATE_EVENT_NAME)
        privMxClient.unregisterAllEvents(UPDATE_EVENT_NAME)
    }
}