package com.github.familyconnector.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.github.familyconnector.components.AppIcon
import com.github.familyconnector.components.typography.Headline1
import familyconnector.composeapp.generated.resources.Res
import familyconnector.composeapp.generated.resources.app_name
import org.jetbrains.compose.resources.stringResource

class InitialScreen : Screen {
    @Composable
    override fun Content() {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(64.dp))
            AppIcon()
            Spacer(modifier = Modifier.height(40.dp))
            Headline1(stringResource(Res.string.app_name))
        }
    }
}