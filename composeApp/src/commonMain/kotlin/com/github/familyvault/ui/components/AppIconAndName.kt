package com.github.familyvault.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.github.familyvault.ui.components.typography.Headline1
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.app_name
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppIconAndName() {
    Column(
        modifier = Modifier
            .padding(vertical = AdditionalTheme.spacings.large)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.large)
    ) {
        AppIcon()
        Headline1(stringResource(Res.string.app_name))
    }
}