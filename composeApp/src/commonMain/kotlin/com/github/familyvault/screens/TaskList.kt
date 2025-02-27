package com.github.familyvault.screens

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.github.familyvault.components.typography.Headline3

class TaskList : Screen {
    @Composable
    override fun Content() {

        Headline3("Task list screen")
    }

}