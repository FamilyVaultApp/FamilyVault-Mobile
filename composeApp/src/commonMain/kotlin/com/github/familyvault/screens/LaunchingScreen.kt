package com.github.familyvault.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.components.screen.StartScreen
import com.github.familyvault.components.typography.Paragraph
import com.github.familyvault.screens.initial.DebugScreenContextId
import com.github.familyvault.screens.initial.InitialScreen
import com.github.familyvault.services.IFamilyGroupService
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.loading
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

class LaunchingScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val familyGroupService = koinInject<IFamilyGroupService>()
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(familyGroupService) {
            coroutineScope.launch {
                if (familyGroupService.assignDefaultStoredFamilyGroup()) {
                    navigator.replaceAll(DebugScreenContextId())
                } else {
                    navigator.replaceAll(InitialScreen())
                }
            }
        }

        StartScreen {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
                Paragraph(
                    stringResource(Res.string.loading),
                    modifier = Modifier.padding(AdditionalTheme.spacings.medium)
                )
            }
        }

    }
}