package com.github.familyvault.screens.main

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.components.AppNavigationBar
import com.github.familyvault.components.typography.Headline3

class ChatsMainScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        Scaffold(
            bottomBar = { AppNavigationBar(navigator) }
        )
        {
            Headline3("Chat main screen")
        }
    }
}