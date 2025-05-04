package com.github.familyvault.ui.screens.main.chat

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.models.chat.ChatThread
import com.github.familyvault.services.IChatService
import com.github.familyvault.services.listeners.IChatMessagesListenerService
import com.github.familyvault.services.listeners.IChatThreadListenerService
import com.github.familyvault.states.ICurrentChatThreadsState
import com.github.familyvault.ui.components.LoaderWithText
import com.github.familyvault.ui.components.ParagraphStickyHeader
import com.github.familyvault.ui.components.chat.ChatThreadEntry
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.chat_type_group
import familyvault.composeapp.generated.resources.chat_type_individual
import familyvault.composeapp.generated.resources.loading
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SelectChatContent() {
    val chatService = koinInject<IChatService>()
    val chatThreadListenerService = koinInject<IChatThreadListenerService>()
    val chatMessagesListenerService = koinInject<IChatMessagesListenerService>()
    val currentChatThreadsState = koinInject<ICurrentChatThreadsState>()

    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        isLoading = true

        withContext(Dispatchers.IO) {
            currentChatThreadsState.clean()

            val groupChats = chatService.retrieveAllGroupChatThreads()
            val individualChats = chatService.retrieveAllIndividualChatThreads()

            currentChatThreadsState.addGroupChatThreads(groupChats)
            currentChatThreadsState.addIndividualChatThreads(individualChats)

            chatThreadListenerService.startListeningForNewChatThread {
                currentChatThreadsState.addNewChatThread(it)
                chatMessagesListenerService.startListeningForNewMessage(it.id) { newMessage ->
                    currentChatThreadsState.editExistingChatThreadLastMessage(newMessage, it)
                }
            }

            chatThreadListenerService.startListeningForUpdatedChatThread {
                currentChatThreadsState.editExistingChatThread(it)
            }

            for (chatThread in currentChatThreadsState.allChatThreads) {
                chatMessagesListenerService.startListeningForNewMessage(chatThread.id) { newMessage ->
                    currentChatThreadsState.editExistingChatThreadLastMessage(newMessage, chatThread)
                }
            }
        }

        isLoading = false
    }

    DisposableEffect(Unit) {
        onDispose {
            chatThreadListenerService.unregisterAllListeners()
            chatMessagesListenerService.unregisterAllListeners()
        }
    }

    if (isLoading) {
        return LoaderWithText(stringResource(Res.string.loading), modifier = Modifier.fillMaxSize())
    }

    LazyColumn {
        stickyHeader {
            ParagraphStickyHeader(
                stringResource(Res.string.chat_type_individual)
            )
        }

        items(currentChatThreadsState.sortedIndividualChatThreads) {
            SelectableChatThreadEntry(it)
        }

        stickyHeader {
            ParagraphStickyHeader(
                stringResource(Res.string.chat_type_group)
            )
        }

        items(currentChatThreadsState.sortedGroupChatThreads) {
            SelectableChatThreadEntry(it)
        }
    }
}

@Composable
private fun SelectableChatThreadEntry(chatThread: ChatThread) {
    val navigator = LocalNavigator.currentOrThrow

    ChatThreadEntry(chatThread, unreadMessages = false) {
        navigator.parent?.push(CurrentChatThreadScreen(chatThread))
    }
}