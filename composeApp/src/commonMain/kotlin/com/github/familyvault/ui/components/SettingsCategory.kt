package com.github.familyvault.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.familyvault.ui.components.typography.ParagraphMuted
import com.github.familyvault.ui.theme.AdditionalTheme

@Composable
fun SettingsCategory(title: String, settings: @Composable () -> Unit) {
    Column(
        modifier = Modifier.padding(vertical = AdditionalTheme.spacings.medium)
    ) {
        ParagraphMuted(
            title,
            modifier = Modifier.padding(start = AdditionalTheme.spacings.screenPadding)
        )
        Spacer(modifier = Modifier.height(AdditionalTheme.spacings.medium))
        settings()
    }
}