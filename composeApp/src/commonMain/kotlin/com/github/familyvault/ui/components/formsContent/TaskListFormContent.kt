package com.github.familyvault.ui.components.formsContent

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.github.familyvault.forms.TaskListForm
import com.github.familyvault.ui.components.ValidationErrorMessage
import com.github.familyvault.ui.components.overrides.TextField
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.title
import org.jetbrains.compose.resources.stringResource

@Composable
fun TaskListFormContent(taskListForm: TaskListForm) {
    Column {
        TextField(
            label = stringResource(Res.string.title),
            value = taskListForm.name,
            onValueChange = { taskListForm.setName(it) },
            supportingText = { ValidationErrorMessage(taskListForm.taskListNameValidationError) }
        )
    }
}