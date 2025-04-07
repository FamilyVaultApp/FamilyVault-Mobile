package com.github.familyvault.services

interface IChatListenerService {
    fun startListeningForNewMessage(chatThreadId: String, onNewMessage: () -> Unit)
}