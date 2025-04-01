package com.github.familyvault.ui.screens.start.createFamilyGroup

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.services.IChatService
import com.github.familyvault.services.IFamilyGroupSessionService
import com.github.familyvault.ui.components.InitialScreenButton
import com.github.familyvault.ui.screens.main.MainScreen
import com.github.familyvault.utils.IQrCodeGenerator
import org.koin.compose.koinInject

class DebugScreenContextId : Screen {
    @Composable
    override fun Content() {
        val familyGroupSessionService = koinInject<IFamilyGroupSessionService>()
        val qrCodeGenerator = koinInject<IQrCodeGenerator>()
        val navigator = LocalNavigator.currentOrThrow
        val contextId = familyGroupSessionService.getContextId()
            
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("ContextId")
            Text(
                contextId
            )
            Text("Debug QR code from ContextId:")
            Image(
                bitmap = qrCodeGenerator.generate(contextId),
                contentDescription = "QR Code"
            )

            InitialScreenButton(
                onClick = {
                    navigator.replaceAll(MainScreen())
                }
            )
        }
    }
}