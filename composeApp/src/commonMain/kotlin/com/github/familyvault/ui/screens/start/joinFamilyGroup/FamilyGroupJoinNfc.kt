package com.github.familyvault.ui.screens.start.joinFamilyGroup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.services.IJoinStatusService
import com.github.familyvault.services.INfcService
import com.github.familyvault.states.IJoinFamilyGroupPayloadState
import com.github.familyvault.ui.components.AnimatedNfcBeam
import com.github.familyvault.ui.components.DangerButton
import com.github.familyvault.ui.components.overrides.Button
import com.github.familyvault.ui.components.screen.StartScreenScaffold
import com.github.familyvault.ui.components.typography.Headline1
import com.github.familyvault.ui.components.typography.Headline3
import com.github.familyvault.ui.screens.start.StartScreen
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.cancel_button_content
import familyvault.composeapp.generated.resources.join_family_group_content
import familyvault.composeapp.generated.resources.join_family_group_title
import familyvault.composeapp.generated.resources.show_qr_code_button_content
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

class FamilyGroupJoinNfc() : Screen {

    @Composable
    override fun Content() {

        val joinFamilyGroupPayloadState = koinInject<IJoinFamilyGroupPayloadState>()
        val navigator = LocalNavigator.currentOrThrow
        val nfcService = koinInject<INfcService>()
        val joinTokenService = koinInject<IJoinStatusService>()
        val payload = remember { joinFamilyGroupPayloadState.getPayload() }


        LaunchedEffect(Unit) {
            nfcService.registerApp()
            nfcService.setEmulateMode(payload)
            joinTokenService.waitForNotInitiatedStatus(payload.joinStatusToken)
            navigator.replaceAll(FamilyGroupJoinWaitingScreen())
        }
        // Clean up when leaving the screen
        DisposableEffect(Unit) {
            onDispose {
                nfcService.unregisterApp()
            }
        }

        StartScreenScaffold {
            JoinFamilyGroupHeader()
            Spacer(modifier = Modifier.height(AdditionalTheme.spacings.large))
            JoinFamilyGroupContent()
        }
    }

    @Composable
    private fun JoinFamilyGroupHeader() {
        Box(modifier = Modifier.padding(vertical = AdditionalTheme.spacings.large)) {
            Headline1(
                stringResource(Res.string.join_family_group_title),
                textAlign = TextAlign.Center
            )
        }
    }

    @Composable
    private fun JoinFamilyGroupContent() {
        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedNfcBeam()
            Headline3(
                stringResource(Res.string.join_family_group_content),
                MaterialTheme.colorScheme.onBackground,
                TextAlign.Center,
                Modifier.padding(AdditionalTheme.spacings.normalPadding)
            )
            JoinFamilyGroupContentButtons()
        }
    }

    @Composable
    private fun JoinFamilyGroupContentButtons() {
        val navigator = LocalNavigator.currentOrThrow

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = AdditionalTheme.spacings.large),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.small),
            ) {
                Button(
                    stringResource(Res.string.show_qr_code_button_content),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        navigator.push(DisplayFamilyMemberDataQrCodeScreen())
                    },
                )
                DangerButton(
                    stringResource(Res.string.cancel_button_content),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { navigator.replaceAll(StartScreen()) },
                )
            }
        }
    }
}