package com.github.familyvault.ui.screens.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
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
import com.github.familyvault.DocumentationLinks
import com.github.familyvault.backend.client.FamilyVaultBackendClient
import com.github.familyvault.backend.client.IFamilyVaultBackendClient
import com.github.familyvault.forms.SelfHostingAddressForm
import com.github.familyvault.models.OptionType
import com.github.familyvault.models.enums.InfoBoxType
import com.github.familyvault.states.ISelfHostedAddressState
import com.github.familyvault.ui.components.ConnectionStatus
import com.github.familyvault.ui.components.ConnectionStatusIndicator
import com.github.familyvault.ui.components.HeaderWithIcon
import com.github.familyvault.ui.components.InfoBox
import com.github.familyvault.ui.components.formsContent.SelfHostingAddressFormContent
import com.github.familyvault.ui.components.overrides.Button
import com.github.familyvault.ui.components.screen.StartScreenScaffold
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.check_connection
import familyvault.composeapp.generated.resources.custom_server
import familyvault.composeapp.generated.resources.documentation
import familyvault.composeapp.generated.resources.next_button_content
import familyvault.composeapp.generated.resources.self_hosting_infobox_content
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

class FamilyGroupSelfHostingScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val backendClient = koinInject<IFamilyVaultBackendClient>()
        val selfHostedAddressState = koinInject<ISelfHostedAddressState>()
        val form = remember { SelfHostingAddressForm() }
        var connectionStatus: ConnectionStatus? by remember { mutableStateOf(null) }
        val testFamilyVaultBackendClient = FamilyVaultBackendClient()
        val coroutineScope = rememberCoroutineScope()

        StartScreenScaffold {
            HeaderWithIcon(
                stringResource(Res.string.custom_server),
                icon = Icons.Outlined.Home,
                type = OptionType.Second
            )
            InfoBox(
                title = stringResource(Res.string.documentation),
                content = stringResource(Res.string.self_hosting_infobox_content),
                type = InfoBoxType.DOCUMENTATION,
                link = DocumentationLinks.SELF_HOSTING
            )
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Bottom
            ) {
                Spacer(modifier = Modifier.height(AdditionalTheme.spacings.small))
                SelfHostingAddressFormContent(form) {
                    connectionStatus = null
                }
                connectionStatus?.let {
                    ConnectionStatusIndicator(it)
                }
                Spacer(modifier = Modifier.height(AdditionalTheme.spacings.medium))
                Column {
                    Button(
                        stringResource(Res.string.check_connection),
                        modifier = Modifier.fillMaxWidth(),
                        enabled = form.isFormValid()
                    ) {
                        testFamilyVaultBackendClient.setCustomBackendUrl(form.address)
                        coroutineScope.launch {
                            connectionStatus = ConnectionStatus.CONNECTING
                            try {
                                testFamilyVaultBackendClient.getSolutionId()
                                connectionStatus = ConnectionStatus.OK
                            } catch (e: Exception) {
                                connectionStatus = ConnectionStatus.FAILED
                                println(e)
                            }
                        }
                    }
                    Button(
                        stringResource(Res.string.next_button_content),
                        modifier = Modifier.fillMaxWidth(),
                        enabled = connectionStatus == ConnectionStatus.OK
                    ) {
                        backendClient.setCustomBackendUrl(form.address)
                        selfHostedAddressState.set(form.address)
                        navigator.push(FamilyGroupCreateOrJoinScreen())
                    }
                }
            }
        }
    }
}