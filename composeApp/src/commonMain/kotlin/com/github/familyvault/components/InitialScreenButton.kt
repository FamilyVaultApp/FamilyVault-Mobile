package com.github.familyvault.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.familyvault.components.overrides.Button
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.next_button_content
import org.jetbrains.compose.resources.stringResource

@Composable
fun InitialScreenButton(
    text: String = stringResource(Res.string.next_button_content),
    enabled: Boolean = true,
    onClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth().padding(
            vertical = AdditionalTheme.spacings.large,
        ),
    ) {
        Button(content = text,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            onClick = { onClick() },
            containerColor = AdditionalTheme.colors.firstOptionPrimaryColor,
            contentColor = AdditionalTheme.colors.contentColor,
            disabledContainerColor = AdditionalTheme.colors.mutedColor,
            disabledContentColor = AdditionalTheme.colors.contentColor
        )
    }
}