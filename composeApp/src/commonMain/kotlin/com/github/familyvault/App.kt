package com.github.familyvault

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import com.github.familyvault.screens.InitialScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        Navigator(InitialScreen())
    }
}