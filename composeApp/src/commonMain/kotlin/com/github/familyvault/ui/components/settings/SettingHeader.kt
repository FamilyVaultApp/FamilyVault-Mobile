package com.github.familyvault.ui.components.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.familyvault.ui.components.typography.ParagraphMuted
import com.github.familyvault.ui.theme.AdditionalTheme

@Composable
fun SettingHeader(
    description: String,
    additionalPaddingOnBottom: Boolean = true,
    modifier: Modifier = Modifier
) {
    val paddingOnBottom = if (additionalPaddingOnBottom) {
        AdditionalTheme.spacings.screenPadding
    } else {
        0.dp
    }

    Column(
        modifier = Modifier.padding(bottom = paddingOnBottom).then(modifier),
        verticalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.medium)
    ) {
        ParagraphMuted(description)
    }
}