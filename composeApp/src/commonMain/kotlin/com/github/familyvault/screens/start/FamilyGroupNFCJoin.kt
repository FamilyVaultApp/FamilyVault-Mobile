package com.github.familyvault.screens.start

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
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.components.getNFCManager
import com.github.familyvault.components.overrides.Button
import com.github.familyvault.components.screen.StartScreenScaffold
import com.github.familyvault.components.typography.Headline1
import com.github.familyvault.components.typography.Headline3
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.cancel_button_content
import familyvault.composeapp.generated.resources.join_family_group_content
import familyvault.composeapp.generated.resources.join_family_group_title
import familyvault.composeapp.generated.resources.show_qr_code_button_content
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

class FamilyGroupNFCJoin(private val newMemberData: String) : Screen {
    @Composable
    override fun Content() {
        StartScreenScaffold {
            val scope = rememberCoroutineScope()
            val nfcManager = getNFCManager()

            val trigger = remember { mutableStateOf(true) }
            val showDialog = remember { mutableStateOf(false) }

            val nfcData = remember { mutableStateOf(newMemberData) }

            scope.launch {
                nfcManager.tags.collectLatest { tagData ->
                    println("Test: I have detected a tag  $tagData")
                    nfcData.value = tagData
                    showDialog.value = true
                }
            }

            if (trigger.value) {
                nfcManager.registerApp()
                trigger.value = false
            }

            JoinFamilyGroupHeader()
            Column {
                Text("NFC tag value is: ")
                Text(nfcData.value)
            }
            Spacer(modifier = Modifier.height(AdditionalTheme.spacings.large))
            JoinFamilyGroupContent(nfcData.value)
        }
    }

    @Composable
    private fun JoinFamilyGroupHeader() {
        Box(
            modifier = Modifier.padding(vertical = AdditionalTheme.spacings.large)
        ) {
            Headline1(
                stringResource(Res.string.join_family_group_title),
                textAlign = TextAlign.Center,
            )
        }
    }

    @Composable
    private fun JoinFamilyGroupContent(nfcData: String) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Filled.Sensors,
                contentDescription = stringResource(Res.string.join_family_group_content),
                modifier = Modifier.size(128.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Headline3(
                stringResource(Res.string.join_family_group_content),
                MaterialTheme.colorScheme.onBackground,
                TextAlign.Center,
                Modifier.padding(AdditionalTheme.spacings.normalPadding)
            )

            JoinFamilyGroupContentButtons(nfcData)
        }
    }

    @Composable
    private fun JoinFamilyGroupContentButtons(nfcData: String) {
        val navigator = LocalNavigator.currentOrThrow

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = AdditionalTheme.spacings.large),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    stringResource(Res.string.cancel_button_content),
                    onClick = { navigator.replaceAll(StartScreen()) },
                    modifier = Modifier.weight(1f)
                )
                Button(
                    stringResource(Res.string.show_qr_code_button_content),
                    onClick = {
                        navigator.push(DisplayKeyPairQrCodeScreen(nfcData))
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
