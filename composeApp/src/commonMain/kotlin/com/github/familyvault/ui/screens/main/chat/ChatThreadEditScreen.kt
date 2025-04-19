package com.github.familyvault.ui.screens.main.chat

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.github.familyvault.models.chat.ChatThread
import com.github.familyvault.models.enums.ChatThreadType

class ChatThreadEditScreen(
    private val chatType: ChatThreadType,
    private val chatThread: ChatThread? = null,
) : Screen {
    @Composable
    override fun Content() {
        when (chatType) {
            ChatThreadType.INDIVIDUAL -> TODO("The edit screen for individual chat threads has not been implemented yet")
            ChatThreadType.GROUP -> GroupChatEdit(chatThread)
        }
    }
}