package com.github.familyvault.screens.main

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.github.familyvault.components.AppNavigationBar

class MainScreen : Screen {
    @Composable
    override fun Content() {
        TabNavigator(ChatTab) {
            Scaffold(
                content = { CurrentTab() },
                bottomBar = {
                    AppNavigationBar(ChatTab, FilesCabinetTab, TaskTab)
                }
            )
        }
    }
}
