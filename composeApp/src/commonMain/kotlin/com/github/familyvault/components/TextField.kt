package com.github.familyvault.components

import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TextField(
    value: String = "",
    modifier: Modifier = Modifier,
    label: @Composable () -> Unit,
    onValueChange: (String) -> Unit = {}
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        modifier = modifier
    )
}