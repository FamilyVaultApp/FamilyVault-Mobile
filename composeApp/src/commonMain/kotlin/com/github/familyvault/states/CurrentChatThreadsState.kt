package com.github.familyvault.states

import androidx.compose.runtime.mutableStateListOf
import com.github.familyvault.models.chat.ChatMessage
import com.github.familyvault.models.chat.ChatThread
import com.github.familyvault.models.enums.ChatThreadType
import com.github.familyvault.services.IFamilyGroupSessionService

class CurrentChatThreadsState(private val familyGroupSessionService: IFamilyGroupSessionService) :
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
            ChatThreadType.INDIVIDUAL -> individualChatThreads.add(
                chatThread.copy(
                    name = chatThread.customNameIfIndividualOrDefault(
                        familyGroupSessionService.getCurrentUser().id
                    )
                )
            )

            ChatThreadType.GROUP -> groupChatThreads.add(chatThread)
        }
    }

    override fun editExistingChatThreadLastMessage(
        newMessage: ChatMessage, chatThread: ChatThread
    ) {
        val userId = familyGroupSessionService.getCurrentUser().id
        val chatThreadList = if (chatThread.type == ChatThreadType.INDIVIDUAL) {
            individualChatThreads
        } else {
            groupChatThreads
        }

        chatThreadList.removeAll { it.id.compareTo(chatThread.id) == 0 }
        chatThreadList.add(
            chatThread.copy(
                name = chatThread.customNameIfIndividualOrDefault(userId),
                lastMessage = newMessage,
            )
        )
    }

}