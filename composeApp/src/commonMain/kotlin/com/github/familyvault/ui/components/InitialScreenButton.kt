package com.github.familyvault.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.familyvault.ui.components.overrides.Button
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
        Button(text = text,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            onClick = { onClick() },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = AdditionalTheme.colors.mutedColor,
            disabledContentColor = AdditionalTheme.colors.onMutedColor
        )
    }
}