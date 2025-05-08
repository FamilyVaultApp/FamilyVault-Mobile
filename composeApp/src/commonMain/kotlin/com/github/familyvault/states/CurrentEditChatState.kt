package com.github.familyvault.states

import com.github.familyvault.models.chat.ChatThread

class CurrentEditChatState : ICurrentEditChatState {
    override var currentChatToEdit: ChatThread? = null
        private set

    override fun updateChatToEdit(chatThread: ChatThread) {
        currentChatToEdit = chatThread
    }

    override fun clean() {
        currentChatToEdit = null
    }
}