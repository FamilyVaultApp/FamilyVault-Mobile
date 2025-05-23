package com.github.familyvault.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Error
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.models.enums.ConnectionStatus
import com.github.familyvault.repositories.IFamilyGroupCredentialsRepository
import com.github.familyvault.services.IFamilyGroupSessionService
import com.github.familyvault.ui.components.typography.Paragraph
import com.github.familyvault.ui.screens.LaunchingScreen
import com.github.familyvault.ui.screens.main.ChangeFamilyGroupScreen
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.connection_error_button_content
import familyvault.composeapp.generated.resources.connection_error_content
import familyvault.composeapp.generated.resources.connection_error_title
import familyvault.composeapp.generated.resources.user_not_found_button_content
import familyvault.composeapp.generated.resources.user_not_found_content
import familyvault.composeapp.generated.resources.user_not_found_title
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun ConnectionError(connectionStatus: ConnectionStatus) {
    val navigator = LocalNavigator.currentOrThrow
    val familyGroupSessionService = koinInject<IFamilyGroupSessionService>()
    val familyGroupCredentialsRepository = koinInject<IFamilyGroupCredentialsRepository>()
    val coroutineScope = rememberCoroutineScope()

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderWithIcon(
            if (connectionStatus == ConnectionStatus.UserNotFound) {
                stringResource(Res.string.user_not_found_title)
            } else {
                stringResource(Res.string.connection_error_title)
            },
            Icons.Outlined.Error
        )
        if (connectionStatus == ConnectionStatus.UserNotFound) {
            Paragraph(
                stringResource(Res.string.user_not_found_content),
                textAlign = TextAlign.Center
            )
        } else {
            Paragraph(
                stringResource(Res.string.connection_error_content),
                textAlign = TextAlign.Center
            )
        }
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Bottom
        ) {
            BottomNextButton(
                text = if (connectionStatus == ConnectionStatus.UserNotFound) {
                    stringResource(Res.string.user_not_found_button_content)
                } else {
                    stringResource(Res.string.connection_error_button_content)
                },
                onClick = {
                    when (connectionStatus) {
                        ConnectionStatus.UserNotFound -> {
                            val contextId = familyGroupSessionService.getContextId()
                            coroutineScope.launch {
                                familyGroupCredentialsRepository.deleteCredential(contextId)
                            }
                            navigator.replaceAll(ChangeFamilyGroupScreen())
                        }

                        else -> {
                            navigator.replaceAll(LaunchingScreen())
                        }
                    }
                }
            )
        }
    }
}