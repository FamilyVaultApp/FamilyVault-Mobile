package com.github.familyvault.ui.screens.main.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import com.github.familyvault.models.chat.ChatMessage
import com.github.familyvault.models.chat.ThreadItem
import com.github.familyvault.services.IChatService
import com.github.familyvault.ui.components.chat.ChatEntry
import org.koin.compose.koinInject

@Composable
fun SelectChatContent() {
    val navigator = LocalNavigator.currentOrThrow

    val chatService = koinInject<IChatService>()
    val threadList = remember { mutableListOf<ThreadItem>()}
    var isLoading by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        threadList.addAll(chatService.retrieveAllThreads())

        if (threadList.isEmpty()) {
            chatService.createThread("CZAT", "nowy")
            threadList.addAll(chatService.retrieveAllThreads())
            if (threadList.isNotEmpty()) isLoading = false
        } else {
            isLoading = false
        }
    }

    Column {
        ChatEntry(
            title = "Tester",
            lastMessage = ChatMessage("Paweł", "ggggggggggggggggggggggggggggggggggggggggg"),
            unreadMessages = true,
            onClick = {
                navigator.parent?.push(CurrentChatThreadScreen(threadList[0]))
            })
        ChatEntry(
            title = "Tester1",
            lastMessage = ChatMessage("Szymon", "Witam"),
            unreadMessages = false
        )
    }
}