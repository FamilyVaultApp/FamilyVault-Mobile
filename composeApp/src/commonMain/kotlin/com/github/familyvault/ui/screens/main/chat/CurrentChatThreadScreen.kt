package com.github.familyvault.ui.screens.main.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import com.github.familyvault.models.chat.ChatThread
import com.github.familyvault.services.IChatListenerService
import com.github.familyvault.services.IChatService
import com.github.familyvault.states.ICurrentChatState
import com.github.familyvault.ui.components.chat.ChatInputField
import com.github.familyvault.ui.components.chat.ChatMessageEntry
import com.github.familyvault.ui.components.overrides.TopAppBar
import com.github.familyvault.ui.theme.AdditionalTheme
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

class CurrentChatThreadScreen(private val chatThread: ChatThread) : Screen {
    private lateinit var chatService: IChatService

    @Composable
    override fun Content() {
        chatService = koinInject<IChatService>()
        val chatState = koinInject<ICurrentChatState>()
        val chatListenerService = koinInject<IChatListenerService>()

        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()
        val isAtTop by
        remember { derivedStateOf { listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0 } }

        LaunchedEffect(chatThread) {
            chatState.update(chatThread.id)

            chatService.populateDatabaseWithLastMessages(chatThread.id)
            chatState.populateStateFromService()

            chatListenerService.startListeningForNewMessage(chatThread.id) {
                coroutineScope.launch {
                    scrollToLastMessage(listState, chatState)
                }
            }

            scrollToLastMessage(listState, chatState)
        }

        LaunchedEffect(isAtTop) {
            if (isAtTop && chatState.messages.isNotEmpty()) {
                val currentLookingMessage = chatState.messages[listState.firstVisibleItemIndex]
                chatState.getNextPageFromService()
                listState.scrollToItem(chatState.messages.indexOf(currentLookingMessage))
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
                    state = listState,
                ) {
                    items(items = chatState.messages) { message ->
                        ChatMessageEntry(
                            message
                        )
                    }
                }

                Box(
                    modifier = Modifier.fillMaxWidth().padding(AdditionalTheme.spacings.small)
                ) {
                    ChatInputField(
                        onTextMessageSend = { handleTextMessageSend(it) },
                        onVoiceMessageSend = { handleVoiceMessageSend(it) }
                    )
                }
            }
        }
    }

    private fun handleTextMessageSend(message: String) {
        if (message.isEmpty()) {
            return
        }
        chatService.sendTextMessage(chatThread.id, message, respondToMessageId = "")
    }

    private fun handleVoiceMessageSend(audio: ByteArray) {
        if (audio.isEmpty()) {
            return
        }
        chatService.sendVoiceMessage(chatThread.id, audio)
    }

    private suspend fun scrollToLastMessage(
        listState: LazyListState, chatState: ICurrentChatState
    ) {
        if (chatState.messages.isNotEmpty()) {
            listState.scrollToItem(chatState.messages.lastIndex)
        }
    }
}