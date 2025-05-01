package com.github.familyvault.ui.components.formsContent

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.github.familyvault.forms.TaskForm
import com.github.familyvault.ui.components.ValidationErrorMessage
import com.github.familyvault.ui.components.overrides.TextField
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.description
import familyvault.composeapp.generated.resources.title
import org.jetbrains.compose.resources.stringResource

@Composable
fun TaskFormContent(form: TaskForm) {
    Column {
        TextField(
            label = stringResource(Res.string.title),
            value = form.title,
            supportingText = {
                ValidationErrorMessage(form.titleValidationError)
            },
            onValueChange = { form.setTitle(it) }
        )

        TextField(
            label = stringResource(Res.string.description),
            value = form.description,
            supportingText = {
                ValidationErrorMessage(form.descriptionValidationError)
            },
            onValueChange = { form.setDescription(it) }
        )
    }
}