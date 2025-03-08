package com.github.familyvault.screens.start

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.components.LoaderWithText
import com.github.familyvault.components.screen.StartScreenScaffold
import com.github.familyvault.models.QrCodeScanResponseStatus
import com.github.familyvault.services.IQRCodeScannerService
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.qr_code_scanning
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

class FamilyGroupQRCodeJoin : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val qrCodeScanner = koinInject<IQRCodeScannerService>()
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(qrCodeScanner) {
            coroutineScope.launch {
                val response = qrCodeScanner.scanQRCode()

                if (response.status == QrCodeScanResponseStatus.SUCCESS) {
                    navigator.replaceAll(QRCodeScanDebugScreen(response.content ?: ""))
                } else {
                    navigator.pop()
                }
            }
        }

        StartScreenScaffold {
            if (coroutineScope.isActive) {
                LoaderWithText(
                    stringResource(Res.string.qr_code_scanning), modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}