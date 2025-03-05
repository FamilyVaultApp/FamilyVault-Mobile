package com.github.familyvault.screens.initial

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.components.InitialScreenButton
import com.github.familyvault.components.typography.Paragraph
import com.github.familyvault.qrcodescanner.IQRCodeScanner
import com.github.familyvault.screens.main.MainScreen
import com.github.familyvault.services.IFamilyGroupSessionService
import org.koin.compose.koinInject

class QRCodeScan: Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val qrCodeScanner = koinInject<IQRCodeScanner>()
        var scannedCodeRawValue by remember { mutableStateOf("") }


        scannedCodeRawValue = qrCodeScanner.ScanQRCode() // Oczekiwanie na wynik

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Scanned message")
            Text(
                scannedCodeRawValue
            )

            InitialScreenButton(
                onClick = {
                    navigator.replaceAll(MainScreen())
                }
            )
        }
    }
}