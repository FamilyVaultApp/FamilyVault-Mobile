package com.github.familyvault.ui.screens.main.chat

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.models.chat.ChatThread
import com.github.familyvault.models.enums.ChatThreadType
import com.github.familyvault.services.IChatService
import com.github.familyvault.ui.components.LoaderWithText
import com.github.familyvault.ui.components.ParagraphStickyHeader
import com.github.familyvault.ui.components.chat.ChatThreadEntry
import com.github.familyvault.ui.components.dialogs.ChatCreatingDialog
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.chat_type_group
import familyvault.composeapp.generated.resources.chat_type_individual
import familyvault.composeapp.generated.resources.loading
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SelectChatContent() {
    val chatService = koinInject<IChatService>()

    val groupChatThreads = remember { mutableStateListOf<ChatThread>() }
    val individualChatThreads = remember { mutableStateListOf<ChatThread>() }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        isLoading = true

        groupChatThreads.addAll(chatService.retrieveAllGroupChatThreads())
        individualChatThreads.addAll(chatService.retrieveAllIndividualChatThreads())

        isLoading = false
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

        items(individualChatThreads) {
            SelectableChatThreadEntry(it)
        }

        stickyHeader {
            ParagraphStickyHeader(
                stringResource(Res.string.chat_type_group)
            )
        }

        items(groupChatThreads) {
            SelectableChatThreadEntry(it)
        }
    }
}

@Composable
private fun SelectableChatThreadEntry(chatThread: ChatThread) {
    val navigator = LocalNavigator.currentOrThrow
    val chatService = koinInject<IChatService>()
    val coroutineScope = rememberCoroutineScope()

    var isCreatingDialog by mutableStateOf(false)

    if (isCreatingDialog) {
        ChatCreatingDialog()
    }

    ChatThreadEntry(chatThread, unreadMessages = false) {
        coroutineScope.launch {
            if (chatThread.type == ChatThreadType.INDIVIDUAL_DRAFT && !isCreatingDialog) {
                isCreatingDialog = true
                val newChatThread = chatService.createIndividualChatFromDraft(chatThread)
                navigator.parent?.push(CurrentChatThreadScreen(newChatThread))
                isCreatingDialog = false
            } else {
                navigator.parent?.push(CurrentChatThreadScreen(chatThread))
            }
        }
    }
}