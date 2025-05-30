package com.github.familyvault.ui.screens.start.joinFamilyGroup

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.backend.exceptions.FamilyVaultBackendErrorException
import com.github.familyvault.services.IJoinStatusService
import com.github.familyvault.states.IJoinFamilyGroupPayloadState
import com.github.familyvault.ui.components.dialogs.ErrorDialog
import com.github.familyvault.ui.components.screen.StartScreenScaffold
import com.github.familyvault.ui.components.typography.Headline1
import com.github.familyvault.ui.components.typography.Headline3
import com.github.familyvault.ui.theme.AdditionalTheme
import com.github.familyvault.utils.IQrCodeGenerator
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.join_family_group_title
import familyvault.composeapp.generated.resources.qr_code_generation_screen_content
import io.ktor.client.network.sockets.ConnectTimeoutException
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

class DisplayFamilyMemberDataQrCodeScreen : Screen {
    @Composable
    override fun Content() {
        val joinFamilyGroupPayloadState = koinInject<IJoinFamilyGroupPayloadState>()
        val joinTokenService = koinInject<IJoinStatusService>()
        val qrCodeGenerator = koinInject<IQrCodeGenerator>()
        val navigator = LocalNavigator.currentOrThrow
        val payload = remember { joinFamilyGroupPayloadState.getPayload() }
        var error by remember { mutableStateOf(false) }

        val codeBitmap = try {
            qrCodeGenerator.generate(payload)
        } catch (e: Exception) {
            println(e)
            error = true
            null
        }

        LaunchedEffect(Unit) {
            try {
                joinTokenService.waitForNotInitiatedStatus(payload.joinStatusToken)
                navigator.replaceAll(FamilyGroupJoinWaitingScreen())
            } catch (e: FamilyVaultBackendErrorException) {
                println(e)
                error = true
            } catch (e: ConnectTimeoutException) {
                println(e)
                error = true
            }
        }
        StartScreenScaffold {
            if (error || codeBitmap == null) {
                ErrorDialog { navigator.pop() }
            } else {
                Box(modifier = Modifier.padding(vertical = AdditionalTheme.spacings.large)) {
                    Headline1(
                        stringResource(Res.string.join_family_group_title),
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(AdditionalTheme.spacings.large))
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        bitmap = codeBitmap,
                        contentDescription = "QR Code",
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.FillWidth
                    )
                    Headline3(stringResource(Res.string.qr_code_generation_screen_content))
                }
            }
        }
    }
}