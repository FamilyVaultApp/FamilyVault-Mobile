package com.github.familyvault.ui.screens.start.joinFamilyGroup

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.models.NewFamilyMemberData
import com.github.familyvault.services.IQRCodeService
import com.github.familyvault.ui.components.InitialScreenButton
import com.github.familyvault.ui.components.typography.Headline3
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.qr_code_generation_screen_content
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

class DisplayFamilyMemberDataQrCodeScreen(private val newMemberData: NewFamilyMemberData): Screen {
    @Composable
    override fun Content() {
        val qrCodeGenerationService = koinInject<IQRCodeService>()
        val navigator = LocalNavigator.currentOrThrow

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Headline3(stringResource(Res.string.qr_code_generation_screen_content))
            Image(
                bitmap = qrCodeGenerationService.generateQRCode(Json.encodeToString(newMemberData))!!,
                contentDescription = "QR Code"
            )

            InitialScreenButton(
                onClick = {
                    navigator.replaceAll(FamilyGroupJoinAwaitScreen(newMemberData, newMemberData.joinStatus!!, newMemberData.joinStatus!!.token))
                }
            )
        }
    }
}