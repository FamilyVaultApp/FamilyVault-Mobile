package com.github.familyvault.components.typography

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.github.familyvault.Constants

@Composable
fun Headline1(text: String, modifier: Modifier = Modifier, textAlign: TextAlign = TextAlign.Start) {
    Text(
        text,
        modifier = modifier,
        textAlign = textAlign,
        fontSize = Constants.headline1FontSize,
        fontWeight = FontWeight.Bold,
    )
}