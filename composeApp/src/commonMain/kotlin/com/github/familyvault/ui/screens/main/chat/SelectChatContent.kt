package com.github.familyvault.ui.screens.main.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.github.familyvault.models.chat.ChatMessage
import com.github.familyvault.ui.components.chat.ChatEntry

@Composable
fun SelectChatContent() {
    Column {
        ChatEntry(
            title = "Tester",
            lastMessage = ChatMessage("Paweł", "ggggggggggggggggggggggggggggggggggggggggg"),
            unreadMessages = true,
            onClick = {})
        ChatEntry(
            title = "Tester1",
            lastMessage = ChatMessage("Szymon", "Witam"),
            unreadMessages = false
        )
    }
}