package com.github.familyvault.ui.screens

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.github.familyvault.models.enums.ConnectionStatus
import com.github.familyvault.ui.components.ConnectionError
import com.github.familyvault.ui.components.screen.StartScreenScaffold

class ConnectionFailedScreen(private val connectionStatus: ConnectionStatus): Screen {

    @Composable
    override fun Content() {
        StartScreenScaffold {
            ConnectionError(connectionStatus)
        }
    }

}