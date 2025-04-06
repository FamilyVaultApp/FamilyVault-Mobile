package com.github.familyvault.services

import com.github.familyvault.backend.client.IPrivMxClient
import com.github.familyvault.repositories.IStoredChatMessageRepository
import com.github.familyvault.states.ICurrentChatState
import com.github.familyvault.utils.mappers.MessageItemToChatMessageMapper
import com.github.familyvault.utils.mappers.MessageItemToStoredChatMessageMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatListenerService(
    private val currentChatState: ICurrentChatState,
    private val sessionService: IFamilyGroupSessionService,
    private val privMxClient: IPrivMxClient,
    private val repository: IStoredChatMessageRepository,
) : IChatListenerService {
    private val serviceScope = CoroutineScope(Dispatchers.Default)

    override fun startListeningForNewMessage(chatThreadId: String, onNewMessage: () -> Unit) {
        privMxClient.registerOnMessageCreated(chatThreadId) {
            val chatMessage = MessageItemToChatMessageMapper.map(it, sessionService.getPublicKey())
            val storedChatMessage = MessageItemToStoredChatMessageMapper.map(it, chatThreadId)

            currentChatState.addNewChatMessage(chatMessage)
            serviceScope.launch {
                repository.addNewStoredChatMessage(storedChatMessage)
            }
            onNewMessage()
        }
    }
}