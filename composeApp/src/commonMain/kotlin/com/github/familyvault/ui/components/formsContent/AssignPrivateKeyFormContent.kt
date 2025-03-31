package com.github.familyvault.ui.components.formsContent

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.github.familyvault.forms.PrivateKeyAssignPasswordForm
import com.github.familyvault.ui.components.ValidationErrorMessage
import com.github.familyvault.ui.components.overrides.TextField
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.password_label
import familyvault.composeapp.generated.resources.repeat_password_label
import org.jetbrains.compose.resources.stringResource

@Composable
fun AssignPrivateKeyFormContent(
    form: PrivateKeyAssignPasswordForm,
) {
    Column {
        TextField(
            value = form.password,
            label = stringResource(Res.string.password_label),
            isPassword = true,
            onValueChange = { form.setPassword(it) },
            supportingText = { ValidationErrorMessage(form.passwordValidationError) }

        )
        TextField(
            value = form.repeatPassword,
            label = stringResource(Res.string.repeat_password_label),
            isPassword = true,
            onValueChange = { form.setRepeatPassword(it) },
            supportingText = { ValidationErrorMessage(form.passwordRepeatValidationError) })
    }
}