package com.github.familyvault.ui.screens.main.chat

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.models.chat.ChatThread
import com.github.familyvault.models.enums.ChatThreadType
import com.github.familyvault.services.IChatMessagesListenerService
import com.github.familyvault.services.IChatService
import com.github.familyvault.services.IChatThreadListenerService
import com.github.familyvault.services.IFamilyGroupSessionService
import com.github.familyvault.ui.components.LoaderWithText
import com.github.familyvault.ui.components.ParagraphStickyHeader
import com.github.familyvault.ui.components.chat.ChatThreadEntry
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.chat_type_group
import familyvault.composeapp.generated.resources.chat_type_individual
import familyvault.composeapp.generated.resources.loading
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SelectChatContent() {
    val chatService = koinInject<IChatService>()
    val chatThreadListenerService = koinInject<IChatThreadListenerService>()
    val chatMessagesListenerService = koinInject<IChatMessagesListenerService>()
    val familyGroupSessionService = koinInject<IFamilyGroupSessionService>()

    val groupChatThreads = remember { mutableStateListOf<ChatThread>() }
    val individualChatThreads = remember { mutableStateListOf<ChatThread>() }
    var isLoading by remember { mutableStateOf(true) }
    val user = remember { familyGroupSessionService.getCurrentUser() }

    LaunchedEffect(Unit) {
        isLoading = true

        groupChatThreads.addAll(chatService.retrieveAllGroupChatThreads())
        individualChatThreads.addAll(chatService.retrieveAllIndividualChatThreads())
        chatThreadListenerService.startListeningForNewChatThread {
            when (it.type) {
                ChatThreadType.INDIVIDUAL -> individualChatThreads.add(
                    it.copy(
                        name = it.customNameIfIndividualOrDefault(
                            user.id
                        )
                    )
                )

                ChatThreadType.GROUP -> groupChatThreads.add(it)
            }
        }
        for (chatThread in (groupChatThreads + individualChatThreads)) {
            chatMessagesListenerService.startListeningForNewMessage(chatThread.id) { newMessage ->
                val chatThreadList =
                    if (chatThread.type == ChatThreadType.INDIVIDUAL) individualChatThreads else groupChatThreads

                chatThreadList.removeAll { it.id.compareTo(chatThread.id) == 0 }
                chatThreadList.add(chatThread.copy(lastMessage = newMessage))
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

        items(individualChatThreads.sortedBy { it.lastMessage?.sendDate }.reversed()) {
            SelectableChatThreadEntry(it)
        }

        stickyHeader {
            ParagraphStickyHeader(
                stringResource(Res.string.chat_type_group)
            )
        }

        items(groupChatThreads.sortedBy { it.lastMessage?.sendDate }.reversed()) {
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