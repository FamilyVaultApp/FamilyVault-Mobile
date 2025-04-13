package com.github.familyvault.ui.screens.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.github.familyvault.services.IChatService
import com.github.familyvault.services.IFamilyGroupService
import com.github.familyvault.ui.components.overrides.NavigationBar
import com.github.familyvault.ui.theme.AppTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.chat_create_new
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

class MainScreen : Screen {
    @Composable
    override fun Content() {
        TabNavigator(ChatTab) {
            // Workaround błędu w Jetpack Compose powodujący to, że ekran nie dostostoswuje się
            // dynamicznie do motywu systemu. Zostanie naprawiony w jetpack compose 1.8.0-alpha06.
            // TODO: usunąć po naprawieniu blędu w compose.
            AppTheme {
                Scaffold(bottomBar = {
                    NavigationBar(
                        ChatTab, FilesCabinetTab, TaskTab
                    )
                }, floatingActionButton = {
                    FloatingCurrentTabActionButton()
                }) {
                    CurrentTab()
                }
            }
        }
    }

    @Composable
    private fun FloatingCurrentTabActionButton() {
        val tabNavigator = LocalTabNavigator.current

        when (tabNavigator.current) {
            is ChatTab -> FloatingChatActionButton()
        }
    }

    @Composable
    private fun FloatingChatActionButton() {
        FloatingActionButton(onClick = {}) {
            Icon(Icons.Filled.GroupAdd, stringResource(Res.string.chat_create_new))
        }
    }
}
