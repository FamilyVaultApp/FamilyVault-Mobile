package com.github.familyvault.ui.screens.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lan
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.backend.client.FamilyVaultBackendClient
import com.github.familyvault.backend.client.IFamilyVaultBackendClient
import com.github.familyvault.forms.BackendConfigurationForm
import com.github.familyvault.ui.components.BottomNextButton
import com.github.familyvault.ui.components.HeaderWithIcon
import com.github.familyvault.ui.components.InfoBox
import com.github.familyvault.ui.components.ValidationErrorMessage
import com.github.familyvault.ui.components.overrides.TextField
import com.github.familyvault.ui.components.screen.StartScreenScaffold
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.connection_test_content
import familyvault.composeapp.generated.resources.backend_url
import familyvault.composeapp.generated.resources.self_host_server_connection_error_dialog_content
import familyvault.composeapp.generated.resources.confirm_button_content
import familyvault.composeapp.generated.resources.self_host_infobox_title
import familyvault.composeapp.generated.resources.self_host_infobox_content
import familyvault.composeapp.generated.resources.self_host_title
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject


class SelfHostedDataFormScreen: Screen {

    @Composable
    override fun Content() {
        val familyVaultBackendClient = koinInject<IFamilyVaultBackendClient>()
        val form by remember { mutableStateOf(BackendConfigurationForm()) }
        var showErrorDialog by remember { mutableStateOf(false) }
        val testBackend = remember { FamilyVaultBackendClient() }
        val navigator = LocalNavigator.currentOrThrow
        val coroutineScope = rememberCoroutineScope()
        var connectionTestRunning by remember { mutableStateOf(false) }

        DisposableEffect(Unit) {
            onDispose {
                familyVaultBackendClient.clearCustomBackendUrl()
            }
        }

        StartScreenScaffold {
            SelfHostedHeader()

            Column(
                modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Bottom
            ) {
                InfoBoxComponent()
                TextField(
                    value = form.backendUrl,
                    label = stringResource(Res.string.backend_url),
                    onValueChange = { form.setBackendUrl(it) },
                    supportingText = { ValidationErrorMessage(form.backendUrlValidationError) }

                )
                BottomNextButton(
                    text = stringResource(Res.string.connection_test_content),
                    enabled = form.isFormValid() && !connectionTestRunning
                ) {
                    connectionTestRunning = true
                    coroutineScope.launch {
                        try {
                            testBackend.setCustomBackendUrl(form.backendUrl)
                        } catch (e: Exception) {
                            showErrorDialog = true
                        }

                        if (!showErrorDialog) {
                            familyVaultBackendClient.setCustomBackendUrl(form.backendUrl)
                            println(familyVaultBackendClient.getCustomBackendUrl())
                            navigator.push(FamilyGroupCreateOrJoinScreen())
                        }
                        connectionTestRunning = false
                    }
                }
            }
            if (showErrorDialog) {
                BackendConnectionErrorDialog({
                    showErrorDialog = false
                })
            }
        }
    }

    @Composable
    private fun SelfHostedHeader() {
        HeaderWithIcon(
            stringResource(Res.string.self_host_title),
            Icons.Filled.Lan
        )
    }

    @Composable
    fun BackendConnectionErrorDialog(onConfirm: () -> Unit) {
        AlertDialog(
            onDismissRequest = onConfirm,
            text = { Text(stringResource(Res.string.self_host_server_connection_error_dialog_content)) },
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text(stringResource(Res.string.confirm_button_content))
                }
            },
        )
    }

    @Composable
    private fun InfoBoxComponent() {
        InfoBox(
            title = stringResource(Res.string.self_host_infobox_title),
            content = stringResource(Res.string.self_host_infobox_content)
        )
    }
}