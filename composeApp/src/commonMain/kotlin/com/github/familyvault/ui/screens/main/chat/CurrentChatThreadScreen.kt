package com.github.familyvault.ui.screens.main.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import com.github.familyvault.ui.components.chat.ChatInputField
import com.github.familyvault.ui.components.chat.ChatThreadEntry
import com.github.familyvault.ui.components.overrides.TopAppBar

class CurrentChatThreadScreen(private val threadName: String, private val threadContent: List<Pair<String, String>>) :
    Screen {

    @Composable
    override fun Content() {

        Scaffold(
            topBar = {
                TopAppBar(threadName, false)
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
                    items(threadContent.count()) { index ->
                        ChatThreadEntry(threadContent[index].first, threadContent[index].second, index % 2 == 0) // TODO: Poprawna implementacja rozpoznania autora
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    ChatInputField()
                }
            }
        }
    }
}