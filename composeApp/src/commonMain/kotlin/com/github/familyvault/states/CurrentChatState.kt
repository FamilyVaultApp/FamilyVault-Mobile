package com.github.familyvault.states

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.github.familyvault.models.chat.ChatMessage
import com.github.familyvault.models.chat.ChatThread
import com.github.familyvault.services.IChatService

class CurrentChatState(
    private val chatService: IChatService,
) : ICurrentChatState {
    private var currentPage: Int = 0

    override var messages = mutableStateListOf<ChatMessage>()
        private set

    override var chatThread: ChatThread? by mutableStateOf(null)
        private set

    override fun update(chatThread: ChatThread) {
        this.chatThread = chatThread
        messages.clear()
        currentPage = 0
    }

    override suspend fun populateStateFromService() {
        val chatMessagesFromService =
            chatService.retrieveMessagesFirstPage(requireNotNull(chatThread?.id))

        addNewChatMessages(chatMessagesFromService)
    }

    override suspend fun getNextPageFromService() {
        val chatMessagePage =
            chatService.retrieveMessagesPage(requireNotNull(chatThread?.id), currentPage++)

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