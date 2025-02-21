package com.github.familyvault.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.components.AppIconAndName
import com.github.familyvault.components.InfoBox
import com.github.familyvault.components.NextScreenButton
import com.github.familyvault.components.OptionButton
import com.github.familyvault.components.OptionButtonType
import com.github.familyvault.components.screen.StartScreen
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.cloud_connection_mode_content
import familyvault.composeapp.generated.resources.cloud_connection_mode_title
import familyvault.composeapp.generated.resources.connection_modes_content
import familyvault.composeapp.generated.resources.connection_modes_title
import familyvault.composeapp.generated.resources.self_hosted_connection_mode_content
import familyvault.composeapp.generated.resources.self_hosted_connection_mode_title
import org.jetbrains.compose.resources.stringResource

class InitialScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        StartScreen {
            AppIconAndName()
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Bottom
            ) {
                InfoBoxAndButtons()
                NextScreenButton(onClick = { navigator.push(FamilyGroupCreateOrJoinScreen()) })
            }
        }
    }

    @Composable
    private fun InfoBoxAndButtons() {
        Column(
            verticalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.medium)
            
        ) {
            InfoBox(
                title = stringResource(Res.string.connection_modes_title),
                content = stringResource(Res.string.connection_modes_content)
            )
            Spacer(modifier = Modifier.height(AdditionalTheme.spacings.medium))
            OptionButton(
                title = stringResource(Res.string.cloud_connection_mode_title),
                content = stringResource(Res.string.cloud_connection_mode_content),
                Icons.Outlined.Cloud,
                type = OptionButtonType.First
            )
            OptionButton(
                title = stringResource(Res.string.self_hosted_connection_mode_title),
                content = stringResource(Res.string.self_hosted_connection_mode_content),
                Icons.Outlined.Home,
                type = OptionButtonType.Second
            )
        }
    }
}

