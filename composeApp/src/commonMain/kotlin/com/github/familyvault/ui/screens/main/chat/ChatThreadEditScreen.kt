package com.github.familyvault.ui.screens.main.chat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import cafe.adriel.voyager.core.screen.Screen
import com.github.familyvault.models.enums.chat.ChatThreadType
import com.github.familyvault.states.ICurrentEditChatState
import org.koin.compose.koinInject

class ChatThreadEditScreen(
    private val chatType: ChatThreadType,
) : Screen {
    @Composable
    override fun Content() {
        val currentEditChatState = koinInject<ICurrentEditChatState>()

        DisposableEffect(Unit) {
            onDispose {
                currentEditChatState.clean()
            }
        }

        when (chatType) {
            ChatThreadType.INDIVIDUAL -> TODO("The edit screen for individual chat threads has not been implemented yet")
            ChatThreadType.GROUP -> GroupChatEdit()
        }
    }
}