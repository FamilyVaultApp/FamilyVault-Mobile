package com.github.familyvault.services.listeners

import com.github.familyvault.models.chat.ChatMessage

interface IChatMessagesListenerService : IListenerService {
    fun startListeningForNewMessage(
        chatThreadId: String,
        onNewMessage: (newMessage: ChatMessage) -> Unit
    )
}