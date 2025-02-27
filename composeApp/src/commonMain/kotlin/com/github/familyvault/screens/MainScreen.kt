package com.github.familyvault.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import com.github.familyvault.components.BottomNavigationBar

class MainScreen : Screen {
    @Composable
    override fun Content() {
        Navigator(HomeScreen()) { navigator ->
            Scaffold(
                bottomBar = { BottomNavigationBar(navigator) }
            ) { paddingValues ->
                Box(Modifier.padding(paddingValues)) {
                    navigator.lastItem.Content()
                }
            }
        }
    }
}
