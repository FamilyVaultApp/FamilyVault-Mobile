package com.github.familyvault.ui.screens.main.familyGroupSettings.familyGroupMember

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.services.INfcService
import com.github.familyvault.ui.components.AnimatedNfcBeam
import com.github.familyvault.ui.components.overrides.Button
import com.github.familyvault.ui.components.screen.StartScreenScaffold
import com.github.familyvault.ui.components.typography.Headline1
import com.github.familyvault.ui.components.typography.Headline3
import com.github.familyvault.ui.screens.main.MainScreen
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.add_member_to_family_group_content
import familyvault.composeapp.generated.resources.add_member_to_family_group_header
import familyvault.composeapp.generated.resources.cancel_button_content
import familyvault.composeapp.generated.resources.scan_qr_code_button_content
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

class AddMemberToFamilyGroupScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val nfcService = koinInject<INfcService>()

        LaunchedEffect(Unit) {
            nfcService.registerApp()
            nfcService.setReadMode()
            nfcService.tags.collect { payload ->
                if (payload.newMemberData.surname.isNotEmpty()) {
                    navigator.replace(AddMemberToFamilyGroupBackendOperationsScreen(payload))
                } else {
                    println("Error reading data from tag")
                }
            }
        }

        DisposableEffect(Unit) {
            onDispose {
                nfcService.unregisterApp()
            }
        }

        StartScreenScaffold {
            AddMemberToFamilyGroupHeader()
            Spacer(Modifier.height(AdditionalTheme.spacings.large))
            AddMemberToFamilyGroupContent()
        }
    }

    @Composable
    private fun AddMemberToFamilyGroupHeader() {
        Box(Modifier.padding(vertical = AdditionalTheme.spacings.large)) {
            Headline1(
                stringResource(Res.string.add_member_to_family_group_header),
                textAlign = TextAlign.Center
            )
        }
    }

    @Composable
    private fun AddMemberToFamilyGroupContent() {
        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedNfcBeam()
            Headline3(
                stringResource(Res.string.add_member_to_family_group_content),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(AdditionalTheme.spacings.normalPadding)
            )
            AddMemberToFamilyGroupContentButtons()
        }
    }

    @Composable
    private fun AddMemberToFamilyGroupContentButtons() {
        val navigator = LocalNavigator.currentOrThrow

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = AdditionalTheme.spacings.large),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.large),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    stringResource(Res.string.cancel_button_content),
                    onClick = { navigator.replaceAll(MainScreen()) },
                )
                Button(
                    stringResource(Res.string.scan_qr_code_button_content),
                    onClick = { navigator.push(AddMemberToFamilyGroupQrCodeScanScreen()) }
                )
            }
        }
    }
}