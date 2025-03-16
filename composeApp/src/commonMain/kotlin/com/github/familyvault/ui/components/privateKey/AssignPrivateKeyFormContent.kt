package com.github.familyvault.ui.components.privateKey

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.familyvault.forms.PrivateKeyAssignPasswordForm
import com.github.familyvault.ui.components.InitialScreenButton
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
    Column(
        modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Bottom
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
}