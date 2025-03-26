package com.github.familyvault.ui.screens.main.addFamilyMember

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.components.NFCManager
import com.github.familyvault.components.getNFCManager
import com.github.familyvault.models.NewFamilyMemberData
import com.github.familyvault.models.enums.JoinTokenStatus
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

class AddMemberToFamilyGroupScreen : Screen {

    @Composable
    override fun Content() {
        val nfcManager = getNFCManager()
        val navigator = LocalNavigator.currentOrThrow
        val trigger = remember { mutableStateOf(true) }

        val nfcData = remember { mutableStateOf<NewFamilyMemberData?>(null) }
        val showDialog = remember { mutableStateOf(false) }
        val showError = remember { mutableStateOf<String?>(null) }

        if (trigger.value) {
            nfcManager.registerApp()
            trigger.value = false
        }

        // Obserwacja tagów NFC
        LaunchedEffect(Unit) {
            nfcManager.tags.collect { tagData ->
                tagData.joinStatus?.let { status ->
                    when (status.status) {
                        JoinTokenStatus.Error -> {
                            showError.value = status.info?.contextId?.let {
                                "Błąd kontekstu: $it"
                            } ?: "Nieznany błąd podczas odczytu"
                        }
                        else -> {
                            nfcData.value = tagData
                            showDialog.value = true
                        }
                    }
                } ?: run {
                    showError.value = "Nieprawidłowy format danych"
                }
            }
        }

        StartScreenScaffold {
            AddMemberToFamilyGroupHeader()
            Spacer(Modifier.height(AdditionalTheme.spacings.large))
            AddMemberToFamilyGroupContent()
        }

        // Dialog z danymi NFC
        if (showDialog.value) {
            AlertDialog(
                onDismissRequest = { showDialog.value = false },
                title = { Text("Dodaj członka") },
                text = {
                    Text("Czy dodać członka:\n" +
                            "${nfcData.value?.firstname} ${nfcData.value?.surname}\n" +
                            "Klucz publiczny: ${nfcData.value?.keyPair?.publicKey?.take(10)}...")
                },
                confirmButton = {
                    Button("Tak") {
                        navigator.replaceAll(MainScreen())
                    }
                },
                dismissButton = {
                    Button("Anuluj") { showDialog.value = false }
                }
            )
        }


        // Dialog z błędami
        showError.value?.let { error ->
            AlertDialog(
                onDismissRequest = { showError.value = null },
                title = { Text("Błąd NFC") },
                text = { Text(error) },
                confirmButton = {
                    Button("OK") { showError.value = null }
                }
            )
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
                MaterialTheme.colorScheme.onBackground,
                TextAlign.Center,
                Modifier.padding(AdditionalTheme.spacings.normalPadding)
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
                    stringResource(Res.string.scan_qr_code_button_content), onClick = {
                        navigator.push(AddMemberToFamilyGroupQrCodeScanScreen())
                    }
                )
            }
        }
    }
}
