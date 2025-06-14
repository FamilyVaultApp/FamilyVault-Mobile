package com.github.familyvault.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import com.github.familyvault.models.OptionType
import com.github.familyvault.ui.components.typography.Headline1
import com.github.familyvault.ui.theme.AdditionalTheme

@Composable
fun HeaderWithIcon(text: String, icon: ImageVector, type: OptionType = OptionType.First) {
    Column(
        modifier = Modifier.padding(vertical = AdditionalTheme.spacings.large),
        verticalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.large),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Headline1(
            text,
            textAlign = TextAlign.Center,
        )
        HeaderIcon(
            icon,
            type = type
        )
    }
}