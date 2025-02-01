package com.github.familyvault.components.typography

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.github.familyvault.Constants

@Composable
fun Headline3(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight = FontWeight.Normal
) {
    Text(
        text,
        modifier = modifier,
        fontSize = Constants.headline3FontSize,
        fontWeight = fontWeight,
        color = color
    )
}