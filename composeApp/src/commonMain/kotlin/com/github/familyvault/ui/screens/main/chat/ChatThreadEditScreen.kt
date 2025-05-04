package com.github.familyvault.ui.screens.main.chat

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.github.familyvault.models.enums.chat.ChatThreadType

class ChatThreadEditScreen(
    private val chatType: ChatThreadType,
) : Screen {
    @Composable
    override fun Content() {
        when (chatType) {
            ChatThreadType.INDIVIDUAL -> TODO("The edit screen for individual chat threads has not been implemented yet")
            ChatThreadType.GROUP -> GroupChatEdit()
        }
    }
}