package com.github.familyvault.ui.screens.main

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.github.familyvault.ui.components.SettingsIconButton
import com.github.familyvault.ui.components.overrides.TopAppBar
import com.github.familyvault.ui.screens.main.chat.SelectChatContent
import com.github.familyvault.ui.screens.main.familyGroupSettings.FamilyGroupSettingsScreen
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.chat_tab
import org.jetbrains.compose.resources.stringResource

object ChatTab : Tab {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Column {
            TopAppBar(
                stringResource(Res.string.chat_tab),
                actions = {
                    SettingsIconButton {
                        navigator.parent?.push(FamilyGroupSettingsScreen())
                    }
                },
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