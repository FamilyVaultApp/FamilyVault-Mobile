package com.github.familyconnector.components.typography

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.github.familyconnector.Constants

@Composable
fun Paragraph(text: String, modifier: Modifier = Modifier) {
    Text(
        text,
        modifier = modifier,
        fontSize = Constants.headline3FontSize,
        fontWeight = FontWeight.Normal,
        color = Constants.mutedColor
    )
}