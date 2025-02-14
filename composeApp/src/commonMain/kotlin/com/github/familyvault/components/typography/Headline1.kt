package com.github.familyvault.components.typography

import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

@Composable
fun Headline1(
    text: String,
    color: Color = MaterialTheme.colorScheme.onBackground,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        text,
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.Bold,
        color = color,
        textAlign = textAlign,
    )
}