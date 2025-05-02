package com.github.familyvault.services.listeners

import com.github.familyvault.models.chat.ChatThread

interface IChatThreadListenerService : IListenerService {
    fun startListeningForNewChatThread(onNewChatThread: (ChatThread) -> Unit)
    fun startListeningForUpdatedChatThread(onUpdatedChatThread: (ChatThread) -> Unit)
}