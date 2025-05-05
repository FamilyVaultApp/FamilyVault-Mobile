package com.github.familyvault.states

import androidx.compose.runtime.mutableStateListOf
import com.github.familyvault.models.chat.ChatMessage
import com.github.familyvault.services.IChatService

class CurrentChatState(
    private val chatService: IChatService,
) : ICurrentChatState {
    private var chatThreadId: String? = null
    private var currentPage: Int = 0

    override var messages = mutableStateListOf<ChatMessage>()
        private set

    override fun update(chatThreadId: String) {
        messages.clear()
        this.chatThreadId = chatThreadId
        currentPage = 0
    }

    override fun clear() {
        currentPage = 0
        messages.clear()
    }

    override suspend fun populateStateFromService() {
        val chatMessagesFromService =
            chatService.retrieveMessagesFirstPage(requireNotNull(chatThreadId))

        addNewChatMessages(chatMessagesFromService)
    }

    override suspend fun getNextPageFromService() {
        val chatMessagePage =
            chatService.retrieveMessagesPage(requireNotNull(chatThreadId), currentPage++)

        addNewChatMessages(chatMessagePage)
    }

    override fun addNewChatMessage(chatMessage: ChatMessage) {
        if (chatMessage !in messages) {
            messages.add(chatMessage)
        }
        messages.sortBy { it.sendDate }
    }

    override fun addNewChatMessages(chatMessages: List<ChatMessage>) {
        messages.addAll(chatMessages.filter { it !in messages })
        messages.sortBy { it.sendDate }
    }
}