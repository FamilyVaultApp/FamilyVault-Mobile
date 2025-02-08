package com.github.familyvault.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.Constants
import com.github.familyvault.components.AppIcon
import com.github.familyvault.components.Button
import com.github.familyvault.components.InfoBox
import com.github.familyvault.components.OptionButton
import com.github.familyvault.components.OptionButtonType
import com.github.familyvault.components.typography.Headline1
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.app_name
import familyvault.composeapp.generated.resources.cloud_connection_mode_content
import familyvault.composeapp.generated.resources.cloud_connection_mode_title
import familyvault.composeapp.generated.resources.connection_modes_content
import familyvault.composeapp.generated.resources.connection_modes_title
import familyvault.composeapp.generated.resources.next_button_content
import familyvault.composeapp.generated.resources.self_hosted_connection_mode_content
import familyvault.composeapp.generated.resources.self_hosted_connection_mode_title
import org.jetbrains.compose.resources.stringResource

class InitialScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Constants.screenPaddingSize)
                .padding(top = Constants.largeSpacing),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Constants.largeSpacing)
        ) {
            AppIcon()
            Headline1(stringResource(Res.string.app_name))
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(Constants.mediumSpacing)
            ) {
                InfoBox(
                    title = stringResource(Res.string.connection_modes_title),
                    content = stringResource(Res.string.connection_modes_content)
                )
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

                Button(
                    content = stringResource(Res.string.next_button_content),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        navigator.push(FamilyGroupChooseScreen())
                    }
                )
            }
        }
    }
}