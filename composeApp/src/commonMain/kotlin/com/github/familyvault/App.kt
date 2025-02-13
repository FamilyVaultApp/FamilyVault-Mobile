package com.github.familyvault

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.material3.MaterialTheme
import cafe.adriel.voyager.navigator.Navigator
import com.github.familyvault.screens.InitialScreen
import com.github.familyvault.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    AppTheme {
        Scaffold(
            backgroundColor = MaterialTheme.colorScheme.background
        ) {
           Navigator(InitialScreen())
        }
    }
}