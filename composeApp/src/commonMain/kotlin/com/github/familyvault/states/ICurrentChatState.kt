package com.github.familyvault.states

import com.github.familyvault.models.chat.ChatMessage

interface ICurrentChatState {
    fun update(chatThreadId: String)
    suspend fun populateStateFromService()
    fun addNewChatMessages(chatMessages: List<ChatMessage>)
}