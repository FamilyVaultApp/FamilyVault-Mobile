package com.github.familyvault.ui.components.typography

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.github.familyvault.ui.theme.AdditionalTheme

@Composable
fun ParagraphMuted(
    text: String,
    modifier: Modifier = Modifier,
    fontStyle: FontStyle = FontStyle.Normal,
    textStyle: TextStyle = TextStyle.Default,
    textAlign: TextAlign = TextAlign.Start,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Ellipsis
) {
    return Paragraph(
        text,
        AdditionalTheme.colors.onMutedColor,
        modifier = modifier,
        fontStyle = fontStyle,
        textStyle = textStyle,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow
    )
}