package com.github.familyvault.ui.screens.start.joinFamilyGroup

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.models.NewFamilyMemberData
import com.github.familyvault.services.IQRCodeService
import com.github.familyvault.ui.components.InitialScreenButton
import com.github.familyvault.ui.screens.main.MainScreen
import kotlinx.serialization.json.Json
import org.koin.compose.koinInject

class DisplayKeyPairQrCodeScreen(private val newMemberData: NewFamilyMemberData): Screen {
    @Composable
    override fun Content() {
        val qrCodeGenerationService = koinInject<IQRCodeService>()
        val navigator = LocalNavigator.currentOrThrow

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Key pair QR code:")
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