package com.github.familyvault.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import com.github.familyvault.Constants
import com.github.familyvault.components.AppIcon
import com.github.familyvault.components.InfoBox
import com.github.familyvault.components.typography.Headline1
import familyconnector.composeapp.generated.resources.Res
import familyconnector.composeapp.generated.resources.app_name
import familyconnector.composeapp.generated.resources.connection_modes_content
import familyconnector.composeapp.generated.resources.connection_modes_title
import org.jetbrains.compose.resources.stringResource

class InitialScreen : Screen {
    @Composable
    override fun Content() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Constants.screenPaddingSize),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(Constants.largeSpacing))
            AppIcon()
            Spacer(modifier = Modifier.height(Constants.largeSpacing))
            Headline1(stringResource(Res.string.app_name))
            Spacer(modifier = Modifier.height(Constants.largeSpacing))
            InfoBox(title = stringResource(Res.string.connection_modes_title), content = stringResource(Res.string.connection_modes_content))
        }
    }
}