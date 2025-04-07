package com.github.familyvault.ui.components.typography

import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun Paragraph(
    text: String,
    color: Color = Color.Unspecified,
    modifier: Modifier = Modifier
) {
    Text(
        text,
        style = MaterialTheme.typography.bodyLarge,
        color = color,
        modifier = modifier
    )
}