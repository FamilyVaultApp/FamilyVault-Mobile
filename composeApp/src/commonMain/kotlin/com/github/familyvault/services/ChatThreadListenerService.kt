package com.github.familyvault.services

import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.models.chat.ChatThread
import com.github.familyvault.models.enums.ChatThreadType

class ChatThreadListenerService(
    private val privMxClient: IPrivMxClient,
) : IChatThreadListenerService {
    companion object {
        const val EVENT_NAME = "THREAD_CREATE"
    }

    override fun startListeningForNewChatThread(onNewChatThread: (ChatThread) -> Unit) {
        privMxClient.registerOnThreadCreated(EVENT_NAME) {
            onNewChatThread(
                ChatThread(
                    id = it.threadId,
                    name = it.privateMeta.name,
                    participantsIds = it.users,
                    lastMessage = null,
                    type = ChatThreadType.valueOf(it.publicMeta.type)
                )
            )
        }
    }

    override fun unregisterAllListeners() {
        privMxClient.unregisterAllEvents(EVENT_NAME)
    }
}