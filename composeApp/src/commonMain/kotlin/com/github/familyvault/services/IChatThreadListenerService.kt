package com.github.familyvault.services

import com.github.familyvault.models.chat.ChatThread

interface IChatThreadListenerService : IListenerService {
    fun startListeningForNewChatThread(onNewChatThread: (ChatThread) -> Unit)
    fun startListeningForUpdatedChatThread(onUpdatedChatThread: (ChatThread) -> Unit)
}