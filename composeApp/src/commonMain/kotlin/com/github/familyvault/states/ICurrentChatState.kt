package com.github.familyvault.states

import com.github.familyvault.models.chat.ChatMessage

interface ICurrentChatState {
    val messages: List<ChatMessage>

    fun update(chatThreadId: String)
    fun clear()
    suspend fun populateStateFromService()
    suspend fun getNextPageFromService()
    fun addNewChatMessage(chatMessage: ChatMessage)
    fun addNewChatMessages(chatMessages: List<ChatMessage>)
}