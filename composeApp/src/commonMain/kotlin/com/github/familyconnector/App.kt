package com.github.familyconnector

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import com.github.familyconnector.screens.InitialScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("FamilyConnector") }
                )
            }
        ) {
           Navigator(InitialScreen())
        }

    }
}