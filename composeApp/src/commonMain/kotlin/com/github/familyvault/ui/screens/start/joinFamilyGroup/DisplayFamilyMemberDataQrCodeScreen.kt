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
import com.github.familyvault.AppConfig
import com.github.familyvault.models.FamilyMemberJoinStatus
import com.github.familyvault.models.NewFamilyMemberDataPayload
import com.github.familyvault.models.enums.JoinTokenStatus
import com.github.familyvault.services.IJoinTokenService
import com.github.familyvault.services.IQRCodeService
import com.github.familyvault.ui.components.typography.Headline3
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.qr_code_generation_screen_content
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

class DisplayFamilyMemberDataQrCodeScreen(private val newMemberData: NewFamilyMemberDataPayload): Screen {
    @Composable
    override fun Content() {
        val joinTokenService = koinInject<IJoinTokenService>()
        val qrCodeGenerationService = koinInject<IQRCodeService>()
        val navigator = LocalNavigator.currentOrThrow

        var currentJoinInformation by remember { mutableStateOf(FamilyMemberJoinStatus(newMemberData.joinStatus!!.token, JoinTokenStatus.Pending, null)) }

        LaunchedEffect(Unit) {
            do {
                currentJoinInformation = updateTokenStatus(joinTokenService)
            } while (currentJoinInformation.state != JoinTokenStatus.Pending)
            navigator.replaceAll(FamilyGroupJoinAwaitScreen(newMemberData))
        }

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

        }
    }

    private suspend fun updateTokenStatus(joinTokenService: IJoinTokenService): FamilyMemberJoinStatus {
        val joinStatus = joinTokenService.getJoinStatus(newMemberData.joinStatus!!.token)
        delay(AppConfig.BACKEND_REQUEST_INTERVAL_LENGTH_MS)
        return joinStatus
    }
}