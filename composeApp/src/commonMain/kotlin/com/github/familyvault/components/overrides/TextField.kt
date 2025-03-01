package com.github.familyvault.components.overrides

import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun TextField(
    value: String = "",
    enabled: Boolean = true,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    isPassword: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null,
    onValueChange: (String) -> Unit = {}
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        visualTransformation = if (!isPassword) VisualTransformation.None else PasswordVisualTransformation(),
        label = label,
        modifier = modifier,
        supportingText = supportingText,
        singleLine = singleLine,
    )
}