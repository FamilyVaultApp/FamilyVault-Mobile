package com.github.familyvault.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material.Button as MaterialButton

@Composable
fun Button(content: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    return MaterialButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(content)
    }
}