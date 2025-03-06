package com.github.familyvault.screens.initial

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.components.InitialScreenButton
import com.github.familyvault.screens.main.MainScreen

class QRCodeScanDebugScreen(): Screen {

    constructor(scanResult: String) : this() {
        scan = scanResult
    }

    var scan: String = ""
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Scanned message")
            Text(
                scan
            )

            InitialScreenButton(
                onClick = {
                    navigator.replaceAll(MainScreen())
                }
            )
        }
    }
}