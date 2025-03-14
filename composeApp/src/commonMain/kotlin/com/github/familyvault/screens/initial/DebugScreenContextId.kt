package com.github.familyvault.screens.initial

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
import com.github.familyvault.components.InitialScreenButton
import com.github.familyvault.screens.main.MainScreen
import com.github.familyvault.services.IFamilyGroupSessionService
import org.koin.compose.koinInject

class DebugScreenContextId : Screen {
    @Composable
    override fun Content() {
        val familyGroupSessionService = koinInject<IFamilyGroupSessionService>()
        val navigator = LocalNavigator.currentOrThrow

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("ContextId")
            Text(
                familyGroupSessionService.getContextId()
            )

            InitialScreenButton(
                onClick = {
                    navigator.replaceAll(MainScreen())
                }
            )
        }
    }
}