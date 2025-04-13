package com.github.familyvault.services

import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.models.chat.ChatMessage
import com.github.familyvault.repositories.IStoredChatMessageRepository
import com.github.familyvault.states.ICurrentChatState
import com.github.familyvault.utils.mappers.MessageItemToChatMessageMapper
import com.github.familyvault.utils.mappers.MessageItemToStoredChatMessageMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatMessagesListenerService(
    private val currentChatState: ICurrentChatState,
    private val sessionService: IFamilyGroupSessionService,
    private val privMxClient: IPrivMxClient,
    private val repository: IStoredChatMessageRepository,
) : IChatMessagesListenerService {
    companion object {
        const val EVENT_NAME = "NEW_MESSAGE"
    }

    private val serviceScope = CoroutineScope(Dispatchers.Default)

    override fun startListeningForNewMessage(
        chatThreadId: String,
        onNewMessage: (newMessage: ChatMessage) -> Unit
    ) {
        privMxClient.registerOnMessageCreated(EVENT_NAME, chatThreadId) {
            val chatMessage = MessageItemToChatMessageMapper.map(it, sessionService.getPublicKey())
            val storedChatMessage = MessageItemToStoredChatMessageMapper.map(it, chatThreadId)

            currentChatState.addNewChatMessage(chatMessage)
            serviceScope.launch {
                repository.addNewStoredChatMessage(storedChatMessage)
            }
            onNewMessage(chatMessage)
        }
    }

    override fun unregisterAllListeners() {
        privMxClient.unregisterAllEvents(EVENT_NAME)
    }
}