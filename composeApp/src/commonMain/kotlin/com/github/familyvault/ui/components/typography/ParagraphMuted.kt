package com.github.familyvault.ui.components.typography

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.familyvault.ui.theme.AdditionalTheme

@Composable
fun ParagraphMuted(text: String, modifier: Modifier = Modifier) {
    return Paragraph(text, AdditionalTheme.colors.onMutedColor, modifier = modifier)
}