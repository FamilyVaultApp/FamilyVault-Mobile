package com.github.familyvault.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import com.github.familyvault.services.IFamilyGroupContextService
import org.koin.compose.koinInject

class DebugScreenContextId : Screen {
    @Composable
    override fun Content() {
        val familyGroupContext = koinInject<IFamilyGroupContextService>()

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("ContextId")
            Text(
                familyGroupContext.getCurrentFamilyGroupId()
            )
        }
    }
}