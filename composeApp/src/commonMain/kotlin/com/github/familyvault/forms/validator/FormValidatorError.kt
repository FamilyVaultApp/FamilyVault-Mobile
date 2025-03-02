package com.github.familyvault.forms.validator

import androidx.compose.runtime.Composable
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.form_empty_error
import familyvault.composeapp.generated.resources.form_overfill_error
import familyvault.composeapp.generated.resources.form_password_not_equal_error
import familyvault.composeapp.generated.resources.form_too_short_error
import org.jetbrains.compose.resources.stringResource

enum class FormValidatorError {
    TOO_SHORT,
    NOT_EQUAL,
    TOO_LONG,
    IS_EMPTY
}

@Composable
fun validationErrorToString(validationError: FormValidatorError): String {
    return when (validationError) {
        FormValidatorError.TOO_SHORT -> stringResource(
            Res.string.form_too_short_error,
            FormValidatorConfig.MIN_LENGTH
        )
        FormValidatorError.NOT_EQUAL -> stringResource(Res.string.form_password_not_equal_error)
        FormValidatorError.TOO_LONG -> stringResource(
            Res.string.form_overfill_error,
            FormValidatorConfig.MAX_LENGTH
        )
        FormValidatorError.IS_EMPTY -> stringResource(Res.string.form_empty_error)
    }
}