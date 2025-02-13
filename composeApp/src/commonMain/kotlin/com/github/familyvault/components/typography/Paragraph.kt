package com.github.familyvault.components.typography

import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun Paragraph(
    text: String,
    color: Color = MaterialTheme.colorScheme.onSurface,
) {
    Text(
        text,
        style = MaterialTheme.typography.bodyLarge,
        color = color
    )
}