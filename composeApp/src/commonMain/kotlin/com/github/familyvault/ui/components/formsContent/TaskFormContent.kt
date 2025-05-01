package com.github.familyvault.ui.components.formsContent

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.github.familyvault.forms.TaskForm
import com.github.familyvault.ui.components.ValidationErrorMessage
import com.github.familyvault.ui.components.overrides.TextField
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.task_description
import familyvault.composeapp.generated.resources.task_name
import org.jetbrains.compose.resources.stringResource

@Composable
fun TaskFormContent(form: TaskForm) {
    Column {
        TextField(
            label = stringResource(Res.string.task_name),
            value = form.name,
            supportingText = {
                ValidationErrorMessage(form.nameValidationError)
            },
            onValueChange = { form.setName(it) }
        )
        
        TextField(
            label = stringResource(Res.string.task_description),
            value = form.description,
            supportingText = {
                ValidationErrorMessage(form.descriptionValidationError)
            },
            onValueChange = { form.setDescription(it) }
        )
    }
}