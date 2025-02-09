package com.github.familyvault

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.github.familyvault.screens.InitialScreen
import com.github.familyvault.theme.AppTheme

import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    AppTheme {
        Scaffold {
           Navigator(InitialScreen())
        }
    }
}