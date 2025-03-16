package com.github.familyvault.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.github.familyvault.forms.validator.FormValidatorError
import com.github.familyvault.forms.validator.validationErrorToString
import com.github.familyvault.ui.components.typography.Paragraph

@Composable
fun ValidationErrorMessage(error: FormValidatorError?) {
    return Paragraph(
        text = if (error != null) validationErrorToString(error) else "",
        color = Color.Red
    )
}