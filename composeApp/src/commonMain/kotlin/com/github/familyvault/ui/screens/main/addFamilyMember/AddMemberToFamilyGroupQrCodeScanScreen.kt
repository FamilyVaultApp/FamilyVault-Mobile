package com.github.familyvault.ui.screens.main.addFamilyMember

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.services.IQRCodeService
import com.github.familyvault.ui.components.LoaderWithText
import com.github.familyvault.ui.components.screen.StartScreenScaffold
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.qr_code_scanning
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

class AddMemberToFamilyGroupQrCodeScanScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val qrCodeScanner = koinInject<IQRCodeService>()
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            coroutineScope.launch {
                val payload = qrCodeScanner.scanPayload()
                navigator.replaceAll(AddMemberToFamilyGroupBackendOperationsScreen(payload))
            }
        }

        StartScreenScaffold {
            LoaderWithText(
                stringResource(Res.string.qr_code_scanning), modifier = Modifier.fillMaxSize()
            )
        }
    }
}