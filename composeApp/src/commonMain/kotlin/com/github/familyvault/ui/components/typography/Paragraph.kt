package com.github.familyvault.ui.components.typography

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun Paragraph(
    text: String,
    color: Color = Color.Unspecified,
    textAlign: TextAlign = TextAlign.Unspecified,
    fontStyle: FontStyle = FontStyle.Normal,
    textStyle: TextStyle = TextStyle.Default,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    Text(
        text,
        style = MaterialTheme.typography.bodyLarge + textStyle,
        color = color,
        textAlign = textAlign,
        fontStyle = fontStyle,
        modifier = modifier,
        maxLines = maxLines,
        overflow = overflow
    )
}