package com.github.familyvault.ui.screens.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.backend.client.IFamilyVaultBackendClient
import com.github.familyvault.models.OptionType
import com.github.familyvault.DocumentationLinks
import com.github.familyvault.models.enums.ConnectionMode
import com.github.familyvault.states.ISelfHostedAddressState
import com.github.familyvault.ui.components.AppIconAndName
import com.github.familyvault.ui.components.BottomNextButton
import com.github.familyvault.ui.components.InfoBox
import com.github.familyvault.ui.components.OptionButton
import com.github.familyvault.ui.components.screen.StartScreenScaffold
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.cloud_connection_mode_content
import familyvault.composeapp.generated.resources.cloud_connection_mode_title
import familyvault.composeapp.generated.resources.connection_modes_content
import familyvault.composeapp.generated.resources.connection_modes_title
import familyvault.composeapp.generated.resources.self_hosted_connection_mode_content
import familyvault.composeapp.generated.resources.self_hosted_connection_mode_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

class StartScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val selfHostedAddressState = koinInject<ISelfHostedAddressState>()
        val familyVaultBackendClient = koinInject<IFamilyVaultBackendClient>()
        var selectedConnectionMode by remember { mutableStateOf<ConnectionMode?>(null) }

        StartScreenScaffold {
            AppIconAndName()
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Bottom
            ) {
                InfoBoxComponent()
                Spacer(modifier = Modifier.height(AdditionalTheme.spacings.medium))
                OptionButtons(
                    selectedConnectionMode = selectedConnectionMode,
                    onModeSelected = { selectedConnectionMode = it }
                )
                BottomNextButton(
                    enabled = selectedConnectionMode != null,
                    onClick = {
                        when (selectedConnectionMode) {
                            ConnectionMode.Cloud -> {
                                selfHostedAddressState.reset()
                                familyVaultBackendClient.removeCustomBackendUrl()
                                navigator.push(FamilyGroupCreateOrJoinScreen())
                            }

                            ConnectionMode.SelfHosted -> navigator.push(FamilyGroupSelfHostingScreen())
                            null -> {}
                        }

                    }
                )
            }
        }
    }

    @Composable
    private fun InfoBoxComponent() {
        InfoBox(
            title = stringResource(Res.string.connection_modes_title),
            content = stringResource(Res.string.connection_modes_content),
            link = DocumentationLinks.SELF_HOSTING
        )
    }

    @Composable
    private fun OptionButtons(
        selectedConnectionMode: ConnectionMode?,
        onModeSelected: (ConnectionMode) -> Unit
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.medium)
        ) {
            OptionButton(
                title = stringResource(Res.string.cloud_connection_mode_title),
                content = stringResource(Res.string.cloud_connection_mode_content),
                icon = Icons.Outlined.Cloud,
                type = OptionType.First,
                isSelected = selectedConnectionMode == ConnectionMode.Cloud,
                onClick = { onModeSelected(ConnectionMode.Cloud) }
            )
            OptionButton(
                title = stringResource(Res.string.self_hosted_connection_mode_title),
                content = stringResource(Res.string.self_hosted_connection_mode_content),
                icon = Icons.Outlined.Home,
                type = OptionType.Second,
                isSelected = selectedConnectionMode == ConnectionMode.SelfHosted,
                onClick = { onModeSelected(ConnectionMode.SelfHosted) }
            )
        }
    }
}