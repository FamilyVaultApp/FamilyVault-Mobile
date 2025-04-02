package com.github.familyvault.ui.screens.main.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import com.github.familyvault.models.chat.MessageItem
import com.github.familyvault.models.chat.MessagePublicMeta
import com.github.familyvault.models.chat.ThreadId
import com.github.familyvault.models.chat.ThreadItem
import com.github.familyvault.services.IChatService
import com.github.familyvault.ui.components.chat.ChatInputField
import com.github.familyvault.ui.components.chat.ChatMessageBubble
import com.github.familyvault.ui.components.overrides.TopAppBar
import com.github.familyvault.ui.theme.AdditionalTheme
import org.koin.compose.koinInject

class CurrentChatThreadScreen(private val thread: ThreadItem) :
    Screen {

    @Composable
    override fun Content() {
        val chatService = koinInject<IChatService>()
        var isLoadingMessages by remember { mutableStateOf(true) }
        var messages = remember { mutableListOf<MessageItem>()}
        LaunchedEffect(Unit) {
            // Temporary placeholder messages
            messages.addAll(chatService.retrieveMessagesFromThread(ThreadId(thread.threadId)))
            messages.add(MessageItem("Test", "Osoba1", "", MessagePublicMeta("")))
            messages.add(MessageItem("Test", "Osoba2", "", MessagePublicMeta("")))
            isLoadingMessages = false
        }
        Scaffold(
            topBar = {
                TopAppBar(thread.decodedThreadName, false)
            },
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                LazyColumn (
                    modifier = Modifier
                        .weight(1f)
                )
                {
                    if (!isLoadingMessages) {
                        items(messages.count()) { index ->
                            ChatMessageBubble(
                                messages[index].authorPublicKey,
                                messages[index].messageContent ?: "",
                                index % 2 == 0
                            ) // TODO: Poprawna implementacja rozpoznania autora
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(AdditionalTheme.spacings.small)
                ) {
                    ChatInputField()
                }
            }
        }
    }
}