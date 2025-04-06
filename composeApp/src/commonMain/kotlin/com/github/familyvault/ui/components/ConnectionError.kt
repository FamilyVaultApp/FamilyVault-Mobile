package com.github.familyvault.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Error
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.models.enums.ConnectionStatus
import com.github.familyvault.repositories.IFamilyGroupCredentialsRepository
import com.github.familyvault.services.IFamilyGroupSessionService
import com.github.familyvault.ui.components.typography.Paragraph
import com.github.familyvault.ui.screens.LaunchingScreen
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.connection_error_title
import familyvault.composeapp.generated.resources.user_not_found_title
import familyvault.composeapp.generated.resources.user_not_found_content
import familyvault.composeapp.generated.resources.connection_error_content
import familyvault.composeapp.generated.resources.connection_error_button_content
import familyvault.composeapp.generated.resources.user_not_found_button_content
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun ConnectionError(connectionStatus: ConnectionStatus) {
    val familyGroupSessionService = koinInject<IFamilyGroupSessionService>()
    val familyGroupCredentialsRepository = koinInject<IFamilyGroupCredentialsRepository>()
    val navigator = LocalNavigator.currentOrThrow
    val coroutineScope = rememberCoroutineScope()

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderWithIcon(
            if (connectionStatus == ConnectionStatus.ConnectionError) {
                stringResource(Res.string.connection_error_title)
            } else {
                stringResource(Res.string.user_not_found_title)
            },
            Icons.Outlined.Error
        )
        if (connectionStatus == ConnectionStatus.ConnectionError)
        {
            Paragraph(stringResource(Res.string.connection_error_content))
        } else {
            Paragraph(stringResource(Res.string.user_not_found_content))
        }
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Bottom
        ) {
            InitialScreenButton(
                text = if (connectionStatus == ConnectionStatus.ConnectionError) {
                    stringResource(Res.string.connection_error_button_content)
                } else {
                    stringResource(Res.string.user_not_found_button_content)
                },
                onClick = {
                    if (connectionStatus == ConnectionStatus.UserNotFound) {
                        val contextId = familyGroupSessionService.getContextId()
                        coroutineScope.launch {
                            familyGroupCredentialsRepository.deleteCredential(contextId)
                            navigator.replaceAll(LaunchingScreen())
                        }
                    } else {
                        navigator.replaceAll(LaunchingScreen())
                    }
                }
            )
        }
    }
}