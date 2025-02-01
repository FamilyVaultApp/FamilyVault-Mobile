package com.github.familyvault.components.typography

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.github.familyvault.Constants

@Composable
fun Headline1(text: String, modifier: Modifier = Modifier) {
    Text(
        text,
        modifier = modifier,
        fontSize = Constants.headline1FontSize,
        fontWeight = FontWeight.Bold,
    )
}