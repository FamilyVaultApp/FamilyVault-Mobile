package com.github.familyvault.ui.screens.main.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.models.chat.ChatMessage
import com.github.familyvault.ui.components.chat.ChatEntry

@Composable
fun SelectChatContent() {
    val navigator = LocalNavigator.currentOrThrow
    Column {
        ChatEntry(
            title = "Tester",
            lastMessage = ChatMessage("Paweł", "ggggggggggggggggggggggggggggggggggggggggg"),
            unreadMessages = true,
            onClick = {
                navigator.parent?.push(CurrentChatThreadScreen("Rodzinka w komplecie", listOf("Ja" to "Cześć!", "Inny" to "Hejka!")))
            })
        ChatEntry(
            title = "Tester1",
            lastMessage = ChatMessage("Szymon", "Witam"),
            unreadMessages = false
        )
    }
}