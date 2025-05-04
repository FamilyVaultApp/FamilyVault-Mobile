package com.github.familyvault.states

import com.github.familyvault.models.chat.ChatMessage
import com.github.familyvault.models.chat.ChatThread

interface ICurrentChatState {
    val messages: List<ChatMessage>
    val chatThread: ChatThread?

    fun update(chatThread: ChatThread)
    suspend fun populateStateFromService()
    suspend fun getNextPageFromService()
    fun addNewChatMessage(chatMessage: ChatMessage)
    fun addNewChatMessages(chatMessages: List<ChatMessage>)
}