package com.github.familyvault.states

import androidx.compose.runtime.mutableStateListOf
import com.github.familyvault.models.chat.ChatMessage
import com.github.familyvault.models.chat.ChatThread
import com.github.familyvault.models.enums.chat.ChatThreadType
import com.github.familyvault.services.IFamilyGroupSessionService

class CurrentChatThreadsState() :
    ICurrentChatThreadsState {
    override var groupChatThreads = mutableStateListOf<ChatThread>()
    override var individualChatThreads = mutableStateListOf<ChatThread>()

    override val allChatThreads
        get() = (groupChatThreads + individualChatThreads)
    override val sortedGroupChatThreads: List<ChatThread>
        get() = groupChatThreads.sortedBy { it.lastMessage?.sendDate }.reversed()
    override val sortedIndividualChatThreads: List<ChatThread>
        get() = individualChatThreads.sortedBy { it.lastMessage?.sendDate }.reversed()

    override fun clean() {
        groupChatThreads.clear()
        individualChatThreads.clear()
    }

    override fun addGroupChatThreads(chatThreads: List<ChatThread>) {
        groupChatThreads.addAll(chatThreads)
    }

    override fun addIndividualChatThreads(chatThreads: List<ChatThread>) {
        individualChatThreads.addAll(chatThreads)
    }

    override fun addNewChatThread(chatThread: ChatThread) {
        when (chatThread.type) {
            ChatThreadType.INDIVIDUAL -> individualChatThreads.add(chatThread)
            ChatThreadType.GROUP -> groupChatThreads.add(chatThread)
        }
    }


    override fun editExistingChatThreadLastMessage(
        newMessage: ChatMessage, chatThread: ChatThread
    ) {
        val chatThreadList = if (chatThread.type == ChatThreadType.INDIVIDUAL) {
            individualChatThreads
        } else {
            groupChatThreads
        }

        chatThreadList.removeAll { it.id.compareTo(chatThread.id) == 0 }
        chatThreadList.add(
            chatThread.copy(
                lastMessage = newMessage,
            )
        )
    }

    override fun editExistingChatThread(chatThread: ChatThread) {
        val chatThreadList = if (chatThread.type == ChatThreadType.INDIVIDUAL) {
            individualChatThreads
        } else {
            groupChatThreads
        }
        val editedChatThread = chatThreadList.firstOrNull { it.id == chatThread.id }

        editedChatThread?.let {
            chatThreadList.remove(it)
        }

        chatThreadList.add(if (editedChatThread == null) chatThread else chatThread.copy(lastMessage = editedChatThread.lastMessage))
    }
}