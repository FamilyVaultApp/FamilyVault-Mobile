package com.github.familyvault

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.github.familyvault.ui.screens.LaunchingScreen
import com.github.familyvault.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext

@Composable
@Preview
fun App() {
    KoinContext {
        AppTheme {
            Navigator(LaunchingScreen())
        }
    }
}