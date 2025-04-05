package com.github.familyvault.ui.screens.main.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import com.github.familyvault.models.chat.ChatMessage
import com.github.familyvault.models.chat.ChatThread
import com.github.familyvault.services.IChatService
import com.github.familyvault.ui.components.chat.ChatInputField
import com.github.familyvault.ui.components.chat.ChatMessageEntry
import com.github.familyvault.ui.components.overrides.TopAppBar
import com.github.familyvault.ui.theme.AdditionalTheme
import org.koin.compose.koinInject

class CurrentChatThreadScreen(private val chatThread: ChatThread) : Screen {
    private lateinit var chatService: IChatService

    @Composable
    override fun Content() {
        chatService = koinInject<IChatService>()
        val messages = remember { mutableStateListOf<ChatMessage>() }
        val listState = rememberLazyListState()

        LaunchedEffect(chatService) {
            chatService.populateDatabaseWithLastMessages(chatThread.id)
            messages.addAll(
                chatService.retrieveMessagesLastPage(chatThread.id)
            )
        }

        LaunchedEffect(Unit) {
            if (messages.isNotEmpty()) {
                listState.scrollToItem(messages.lastIndex)
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(chatThread.name, false)
            },
        ) { paddingValues ->
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues)
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    state = listState
                ) {
                    items(items = messages) { message ->
                        ChatMessageEntry(
                            message
                        )
                    }
                }

                Box(
                    modifier = Modifier.fillMaxWidth().padding(AdditionalTheme.spacings.small)
                ) {
                    ChatInputField(onTextMessageSend = { handleTextMessageSend(it) })
                }
            }
        }
    }

    private fun handleTextMessageSend(message: String) {
        chatService.sendMessage(chatThread.id, message, respondToMessageId = "")
    }
}