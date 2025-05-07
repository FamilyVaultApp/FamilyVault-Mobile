package com.github.familyvault.states

import com.github.familyvault.models.chat.ChatThread

interface ICurrentEditChatState {
    val currentChatToEdit: ChatThread?

    fun updateChatToEdit(chatThread: ChatThread)
    fun clean()
}