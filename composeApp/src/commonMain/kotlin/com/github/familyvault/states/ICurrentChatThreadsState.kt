package com.github.familyvault.states

import com.github.familyvault.models.chat.ChatMessage
import com.github.familyvault.models.chat.ChatThread

interface ICurrentChatThreadsState {
    val groupChatThreads: List<ChatThread>
    val individualChatThreads: List<ChatThread>
    val allChatThreads: List<ChatThread>
    val sortedGroupChatThreads: List<ChatThread>
    val sortedIndividualChatThreads: List<ChatThread>

    fun clean()
    fun addGroupChatThreads(chatThreads: List<ChatThread>)
    fun addIndividualChatThreads(chatThreads: List<ChatThread>)
    fun addNewChatThread(chatThread: ChatThread)
    fun editExistingChatThreadLastMessage(newMessage: ChatMessage, chatThread: ChatThread)
    fun editExistingChatThread(chatThread: ChatThread)
}