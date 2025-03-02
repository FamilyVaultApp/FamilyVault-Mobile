package com.github.familyvault.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.github.familyvault.components.typography.Paragraph
import com.github.familyvault.forms.validator.FormValidatorError
import com.github.familyvault.forms.validator.validationErrorToString

@Composable
fun ValidationErrorMessage(error: FormValidatorError?) {
    return Paragraph(
        text = if (error != null) validationErrorToString(error) else "",
        color = Color.Red
    )
}