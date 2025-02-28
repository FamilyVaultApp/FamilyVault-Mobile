package com.github.familyvault.screens

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.github.familyvault.components.typography.Headline3

class ChatsMainScreen : Screen {
    @Composable
    override fun Content() {
        Headline3("Chat main screen")
    }
}