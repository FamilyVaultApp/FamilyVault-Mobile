package com.github.familyvault.ui.components.overrides

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.github.familyvault.ui.components.typography.Paragraph

@Composable
fun TextField(
    label: String,
    modifier: Modifier = Modifier,
    value: String = "",
    enabled: Boolean = true,
    isPassword: Boolean = false,
    onValueChange: (String) -> Unit = {},
    supportingText: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        enabled = enabled,
        modifier = modifier.fillMaxWidth().then(modifier),
        value = value,
        label = { Paragraph(label) },
        supportingText = supportingText,
        onValueChange = onValueChange,
        singleLine = true,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = if (isPassword) KeyboardOptions(keyboardType = KeyboardType.Password) else KeyboardOptions.Default,    )
}