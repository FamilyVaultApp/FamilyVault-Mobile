package com.github.familyvault.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.models.enums.ConnectionStatus
import com.github.familyvault.services.IFamilyGroupService
import com.github.familyvault.ui.components.LoaderWithText
import com.github.familyvault.ui.components.screen.StartScreenScaffold
import com.github.familyvault.ui.screens.start.ConnectionFailedScreen
import com.github.familyvault.ui.screens.start.StartScreen
import com.github.familyvault.ui.screens.start.createFamilyGroup.DebugScreenContextId
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
                val connectionStatus = familyGroupService.assignDefaultStoredFamilyGroup()
                if (connectionStatus == ConnectionStatus.Success) {
                    navigator.replaceAll(DebugScreenContextId())
                } else if (connectionStatus == ConnectionStatus.NoCredentials) {
                    navigator.replaceAll(StartScreen())
                } else {
                    navigator.replaceAll(ConnectionFailedScreen(connectionStatus))
                }
            }
        }

        StartScreenScaffold {
            LoaderWithText(
                stringResource(Res.string.loading), modifier = Modifier.fillMaxSize()
            )
        }
    }
}