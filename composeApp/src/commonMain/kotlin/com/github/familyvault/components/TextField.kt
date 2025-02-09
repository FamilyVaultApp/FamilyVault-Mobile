package com.github.familyvault.components

import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TextField(value: String = "", onValueChange: (String) -> Unit = {}, label: @Composable () -> Unit,
              modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        modifier = modifier
    )
}