package com.github.familyvault.ui.screens.main.chat

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.models.chat.ChatThread
import com.github.familyvault.services.IChatService
import com.github.familyvault.services.IFamilyGroupService
import com.github.familyvault.ui.components.chat.ChatThreadEntry
import org.koin.compose.koinInject

@Composable
fun SelectChatContent() {
    val navigator = LocalNavigator.currentOrThrow

    val chatService = koinInject<IChatService>()
    val familyGroupService = koinInject<IFamilyGroupService>()
    val chatThreads = remember { mutableStateListOf<ChatThread>() }
    var isLoading by mutableStateOf(true)

    LaunchedEffect(Unit) {
        isLoading = true

        chatThreads.clear()
        chatThreads.addAll(
            chatService.retrieveAllChatThreads()
        )

        chatService.createGroupChat(
            "Internaziomale", familyGroupService.retrieveFamilyGroupMembersList()
        )

        isLoading = false
    }

    LazyColumn {
        items(items = chatThreads) {
            ChatThreadEntry(it, unreadMessages = false) {
                navigator.parent?.push(CurrentChatThreadScreen(it))
            }
        }
    }
}