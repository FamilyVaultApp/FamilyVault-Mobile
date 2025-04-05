package com.github.familyvault.ui.screens.main

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.github.familyvault.ui.components.overrides.TopAppBar
import com.github.familyvault.ui.screens.main.chat.SelectChatContent
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.chat_tab
import org.jetbrains.compose.resources.stringResource

object ChatTab : Tab {
    @Composable
    override fun Content() {

        Column {
            TopAppBar(
                stringResource(Res.string.chat_tab)
            )
            SelectChatContent()
        }
    }

    override val options: TabOptions
        @Composable get() {
            val title = stringResource(Res.string.chat_tab)
            val icon = rememberVectorPainter(Icons.AutoMirrored.Filled.Chat)

            return remember {
                TabOptions(
                    index = 0u, title = title, icon = icon
                )
            }
        }
}