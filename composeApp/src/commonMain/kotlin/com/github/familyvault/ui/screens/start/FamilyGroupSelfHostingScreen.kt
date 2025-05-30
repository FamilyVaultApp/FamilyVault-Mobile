package com.github.familyvault.ui.screens.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.outlined.Home
import androidx.compose.runtime.Composable
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
import com.github.familyvault.forms.SelfHostingAddressForm
import com.github.familyvault.models.OptionType
import com.github.familyvault.ui.components.ConnectionStatus
import com.github.familyvault.ui.components.ConnectionStatusIndicator
import com.github.familyvault.ui.components.HeaderWithIcon
import com.github.familyvault.ui.components.formsContent.SelfHostingAddressFormContent
import com.github.familyvault.ui.components.overrides.Button
import com.github.familyvault.ui.components.screen.StartScreenScaffold
import com.github.familyvault.ui.components.typography.Headline1
import com.github.familyvault.ui.theme.AdditionalTheme
import kotlinx.coroutines.launch

class FamilyGroupSelfHostingScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val form = remember { SelfHostingAddressForm() }
        var connectionStatus: ConnectionStatus? by remember { mutableStateOf(null) }
        val testFamilyVaultBackendClient = FamilyVaultBackendClient()
        val coroutineScope = rememberCoroutineScope()

        StartScreenScaffold {
            HeaderWithIcon(
                "Własny serwer",
                icon = Icons.Outlined.Home,
                type = OptionType.Second
            )
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Bottom
            ) {
                SelfHostingAddressFormContent(form){
                    connectionStatus = null
                }
                connectionStatus?.let {
                    ConnectionStatusIndicator(it)
                }
                Spacer(modifier = Modifier.height(AdditionalTheme.spacings.large))
                Column {
                    Button(
                        "Sprawdź połączenie",
                        modifier = Modifier.fillMaxWidth(),
                        enabled = form.isFormValid()
                    ) {
                        testFamilyVaultBackendClient.setCustomServerUrl(form.address)
                        coroutineScope.launch {
                            connectionStatus = ConnectionStatus.CONNECTING
                            try {
                                testFamilyVaultBackendClient.getSolutionId()
                                connectionStatus = ConnectionStatus.OK
                            } catch (_: Exception) {
                                connectionStatus = ConnectionStatus.FAILED
                            }
                        }
                    }
                    Button(
                        "Dalej",
                        modifier = Modifier.fillMaxWidth(),
                        enabled = connectionStatus == ConnectionStatus.OK
                    ) {
                        navigator.push(FamilyGroupCreateOrJoinScreen())
                    }
                }
            }
        }
    }
}