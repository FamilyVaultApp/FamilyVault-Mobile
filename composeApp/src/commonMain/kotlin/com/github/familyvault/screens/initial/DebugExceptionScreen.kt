package com.github.familyvault.screens.initial

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

class DebugExceptionScreen(private val exception: Exception) : Screen {
    @Composable
    override fun Content() {
        Text(
            exception.toString()
        )
    }
}