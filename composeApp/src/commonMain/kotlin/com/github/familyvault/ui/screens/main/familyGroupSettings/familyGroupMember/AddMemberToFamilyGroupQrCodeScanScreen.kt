package com.github.familyvault.ui.screens.main.familyGroupSettings.familyGroupMember

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.exceptions.QrCodeBadScanException
import com.github.familyvault.exceptions.QrCodeCancellationException
import com.github.familyvault.exceptions.QrCodeScannerErrorException
import com.github.familyvault.exceptions.QrCodeScannerNotInstalledException
import com.github.familyvault.services.IQRCodeService
import com.github.familyvault.ui.components.LoaderWithText
import com.github.familyvault.ui.components.dialogs.ErrorDialog
import com.github.familyvault.ui.components.screen.StartScreenScaffold
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.invalid_qr_code_label
import familyvault.composeapp.generated.resources.qr_code_scanner_not_installed
import familyvault.composeapp.generated.resources.qr_code_scanning
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

class AddMemberToFamilyGroupQrCodeScanScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val qrCodeScanner = koinInject<IQRCodeService>()
        var invalidQrCode by remember { mutableStateOf(false) }
        var scanning by remember { mutableStateOf(true) }
        var scannerNotInstalled by remember { mutableStateOf(false) }
        var errorOccurred by remember { mutableStateOf(false) }

        LaunchedEffect(scanning) {
            if (scanning) {
                try {
                    val payload = qrCodeScanner.scanPayload()
                    navigator.replace(AddMemberToFamilyGroupBackendOperationsScreen(payload))
                } catch (e: QrCodeScannerNotInstalledException) {
                    scannerNotInstalled = true
                } catch (e: QrCodeCancellationException) {
                    navigator.pop()
                } catch (e: QrCodeBadScanException) {
                    invalidQrCode = true
                    scanning = false
                    println(e)
                } catch (e: QrCodeScannerErrorException) {
                    errorOccurred = true
                    println(e)
                }
            }
        }

        StartScreenScaffold {
            if (invalidQrCode) {
                ErrorDialog(stringResource(Res.string.invalid_qr_code_label)) {
                    invalidQrCode = false
                    scanning = true
                }
            }
            if (scannerNotInstalled) {
                ErrorDialog(stringResource(Res.string.qr_code_scanner_not_installed)) {
                    navigator.pop()
                }
            }
            if (errorOccurred) {
                ErrorDialog {
                    navigator.pop()
                }
            }
            LoaderWithText(
                stringResource(Res.string.qr_code_scanning), modifier = Modifier.fillMaxSize()
            )
        }
    }
}