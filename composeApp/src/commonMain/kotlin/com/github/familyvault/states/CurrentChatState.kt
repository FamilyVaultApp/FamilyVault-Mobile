package com.github.familyvault.states

import com.github.familyvault.models.chat.ChatMessage
import com.github.familyvault.services.IChatService

class CurrentChatState(
    private val chatService: IChatService,
) : ICurrentChatState {
    private var chatThreadId: String? = null
    private var messages = mutableListOf<ChatMessage>()

    override fun update(chatThreadId: String) {
        this.chatThreadId = chatThreadId
        messages.clear()
    }

    override suspend fun populateStateFromService() {
        val chatMessagesFromService =
            chatService.retrieveMessagesLastPage(requireNotNull(chatThreadId))

        addNewChatMessages(chatMessagesFromService)
    }

    override fun addNewChatMessages(chatMessages: List<ChatMessage>) {
        messages.addAll(chatMessages.filter { it !in messages })
    }
}