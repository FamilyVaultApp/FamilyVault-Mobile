package com.github.familyconnector.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

class InitialScreen : Screen {
    @Composable
    override fun Content() {
        Column {
            Text("Welcome in InitialScreen")
        }
    }
}