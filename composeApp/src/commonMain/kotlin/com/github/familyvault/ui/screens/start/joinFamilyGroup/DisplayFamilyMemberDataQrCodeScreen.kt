package com.github.familyvault.ui.screens.start.joinFamilyGroup

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.services.IJoinStatusService
import com.github.familyvault.states.IJoinFamilyGroupPayloadState
import com.github.familyvault.ui.components.dialogs.ErrorDialog
import com.github.familyvault.ui.components.typography.Headline3
import com.github.familyvault.utils.IQrCodeGenerator
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.qr_code_generation_screen_content
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
            } catch (e: Exception) {
                println(e)
                error = true
            }
        }

        if (error || codeBitmap == null) {
            ErrorDialog { navigator.pop() }
            return
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Headline3(stringResource(Res.string.qr_code_generation_screen_content))
            Image(
                bitmap = codeBitmap,
                contentDescription = "QR Code"
            )
        }
    }
}