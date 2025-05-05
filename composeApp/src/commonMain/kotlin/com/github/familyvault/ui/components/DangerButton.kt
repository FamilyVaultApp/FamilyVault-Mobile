package com.github.familyvault.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.familyvault.ui.components.overrides.Button
import com.github.familyvault.ui.theme.AdditionalTheme

@Composable
fun DangerButton(
    text: String,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        text,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        contentColor = AdditionalTheme.colors.dangerButtonContentColor,
        containerColor = AdditionalTheme.colors.dangerButtonBackgroundColor
    )
}