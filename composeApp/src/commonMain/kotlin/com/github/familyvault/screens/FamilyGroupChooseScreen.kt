package com.github.familyvault.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.GroupAdd
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Login
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.core.screen.Screen
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
import familyvault.composeapp.generated.resources.create_new_family_group_content
import familyvault.composeapp.generated.resources.create_new_family_group_title
import familyvault.composeapp.generated.resources.join_existing_family_group_content
import familyvault.composeapp.generated.resources.join_existing_family_group_title
import familyvault.composeapp.generated.resources.self_hosted_connection_mode_content
import familyvault.composeapp.generated.resources.self_hosted_connection_mode_title
import org.jetbrains.compose.resources.stringResource

class FamilyGroupChooseScreen : Screen {
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
                verticalArrangement = Arrangement.Bottom,
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    OptionButton(
                        title = stringResource(Res.string.create_new_family_group_title),
                        content = stringResource(Res.string.create_new_family_group_content),
                        Icons.Outlined.GroupAdd,
                        type = OptionButtonType.First
                    )
                    OptionButton(
                        title = stringResource(Res.string.join_existing_family_group_title),
                        content = stringResource(Res.string.join_existing_family_group_content),
                        Icons.Outlined.Login,
                        type = OptionButtonType.Second
                    )
                }
                Button(
                    "Dalej",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        navigator.push(InitialScreen())
                    }
                )
            }
        }
    }
}