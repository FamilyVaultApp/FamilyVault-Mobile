package com.github.familyvault.screens.initial

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
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.components.overrides.Button
import com.github.familyvault.components.screen.StartScreen
import com.github.familyvault.components.typography.Headline1
import com.github.familyvault.components.typography.Headline3
import com.github.familyvault.screens.main.MainScreen
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.cancel_button_content
import familyvault.composeapp.generated.resources.join_family_group_content
import familyvault.composeapp.generated.resources.join_family_group_title
import familyvault.composeapp.generated.resources.scan_qr_code_button_content
import org.jetbrains.compose.resources.stringResource

class FamilyGroupJoinScreen : Screen {
    @Composable
    override fun Content() {

        StartScreen {
            JoinFamilyGroupHeader()
            Spacer(modifier = Modifier.height(AdditionalTheme.spacings.large))
            JoinFamilyGroupContent()
        }
    }

    @Composable
    private fun JoinFamilyGroupHeader() {
        return Box(
            modifier = Modifier.padding(vertical = AdditionalTheme.spacings.large)
        ) {
            Headline1(
                stringResource(Res.string.join_family_group_title),
                textAlign = TextAlign.Center,
            )
        }
    }

    @Composable
    private fun JoinFamilyGroupContent() {
        return Column(
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

            JoinFamilyGroupContentButtons()
        }
    }

    @Composable
    private fun JoinFamilyGroupContentButtons() {
        val navigator = LocalNavigator.currentOrThrow
        return Column(
            modifier = Modifier.fillMaxSize().padding(bottom = AdditionalTheme.spacings.large),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Button(
                    stringResource(Res.string.cancel_button_content),
                    onClick = { navigator.replaceAll(InitialScreen()) },
                    modifier = Modifier.weight(1f)
                )
                Button(
                    stringResource(Res.string.scan_qr_code_button_content),
                    onClick = { navigator.replaceAll(QRCodeScan()) },
                    modifier = Modifier.weight(1f)
                ) // Placeholder dla komunikacji NFC i odczytu kodu QR
            }
        }
    }
}