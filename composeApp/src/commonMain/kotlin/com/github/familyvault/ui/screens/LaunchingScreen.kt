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
import com.github.familyvault.services.INotificationService
import com.github.familyvault.ui.components.LoaderWithText
import com.github.familyvault.ui.components.screen.StartScreenScaffold
import com.github.familyvault.ui.screens.main.MainScreen
import com.github.familyvault.ui.screens.start.StartScreen
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
        val notificationService = koinInject<INotificationService>()

        LaunchedEffect(Unit) {
            coroutineScope.launch {
                if (!notificationService.checkNotificationPermission()) {
                    notificationService.requestNotificationsPermission()
                }
                when (val connectionStatus = familyGroupService.assignDefaultStoredFamilyGroup()) {
                    ConnectionStatus.Success -> {
                        navigator.replaceAll(MainScreen())
                    }

                    ConnectionStatus.NoCredentials -> {
                        navigator.replaceAll(StartScreen())
                    }

                    else -> {
                        navigator.replaceAll(ConnectionFailedScreen(connectionStatus))
                    }
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