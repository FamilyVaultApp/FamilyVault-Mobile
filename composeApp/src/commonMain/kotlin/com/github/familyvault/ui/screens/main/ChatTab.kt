package com.github.familyvault.ui.screens.main

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.github.familyvault.services.IFamilyGroupSessionService
import com.github.familyvault.ui.components.SettingsIconButton
import com.github.familyvault.ui.components.overrides.TopAppBar
import com.github.familyvault.ui.screens.main.chat.SelectChatContent
import com.github.familyvault.ui.screens.main.familyGroupSettings.FamilyGroupSettingsScreen
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.chat_tab
import familyvault.composeapp.generated.resources.chat_top_bar_greeting
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

object ChatTab : Tab {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val familyGroupSessionService = koinInject<IFamilyGroupSessionService>()
        var currentUserName by remember { mutableStateOf<String?>(null) }

        LaunchedEffect(Unit) {
            currentUserName = familyGroupSessionService.getCurrentUser().firstname
        }
        Column {
            TopAppBar(
                getTitle(currentUserName),
                actions = {
                    SettingsIconButton {
                        navigator.parent?.push(FamilyGroupSettingsScreen())
                    }
                },
            )
            SelectChatContent()
        }
    }

    @Composable
    private fun getTitle(username: String?): String {
        username?.let {
            return stringResource(Res.string.chat_top_bar_greeting, username)
        }
        return stringResource(Res.string.chat_tab)
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