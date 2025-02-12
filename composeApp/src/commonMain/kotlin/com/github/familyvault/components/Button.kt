package com.github.familyvault.components

import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material.Button as MaterialButton

@Composable
fun Button(content: String, modifier: Modifier = Modifier, onClick: () -> Unit, containerColor: Color = MaterialTheme.colorScheme.primary, contentColor: Color = MaterialTheme.colorScheme.onSurface) {
    MaterialButton(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            backgroundColor  = containerColor,
            contentColor = contentColor
        )
    ) {
        Text(content)
    }
}