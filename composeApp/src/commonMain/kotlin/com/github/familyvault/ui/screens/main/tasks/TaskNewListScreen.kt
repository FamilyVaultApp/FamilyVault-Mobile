package com.github.familyvault.ui.screens.main.tasks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Task
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.forms.TasksListForm
import com.github.familyvault.services.ITaskService
import com.github.familyvault.ui.components.ContentWithActionButton
import com.github.familyvault.ui.components.ValidationErrorMessage
import com.github.familyvault.ui.components.dialogs.CircularProgressIndicatorDialog
import com.github.familyvault.ui.components.overrides.Button
import com.github.familyvault.ui.components.overrides.TextField
import com.github.familyvault.ui.components.overrides.TopAppBar
import com.github.familyvault.ui.components.settings.DescriptionSection
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.create_button_content
import familyvault.composeapp.generated.resources.loading
import familyvault.composeapp.generated.resources.task_add_new_list
import familyvault.composeapp.generated.resources.task_add_new_list_description
import familyvault.composeapp.generated.resources.task_list_title
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

class TaskNewListScreen : Screen {
    @Composable
    override fun Content() {
        val localNavigator = LocalNavigator.currentOrThrow
        val tasksService = koinInject<ITaskService>()
        val tasksNewListForm = remember { TasksListForm() }

        var isCreating by remember { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()

        if (isCreating) {
            CircularProgressIndicatorDialog(stringResource(Res.string.loading))
        }

        Scaffold(topBar = {
            TopAppBar(
                stringResource(Res.string.task_add_new_list),
                icon = Icons.Outlined.Task,
            )
        }, content = { paddingValues ->
            ContentWithActionButton(
                modifier = Modifier.padding(paddingValues)
                    .padding(AdditionalTheme.spacings.screenPadding), content = {
                    Column {
                        DescriptionSection(
                            stringResource(Res.string.task_add_new_list_description)
                        )
                        TextField(
                            label = stringResource(Res.string.task_list_title),
                            value = tasksNewListForm.listName,
                            onValueChange = { tasksNewListForm.setTaskListName(it) },
                            supportingText = { ValidationErrorMessage(tasksNewListForm.taskListNameValidationError) }
                        )
                    }
                }, actionButton = {
                    Button(
                        stringResource(Res.string.create_button_content),
                        enabled = tasksNewListForm.isFormValid(),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        coroutineScope.launch {
                            isCreating = true
                            tasksService.createNewTaskList(tasksNewListForm.listName)
                            isCreating = false
                            localNavigator.pop()
                        }
                    }
                })
        })
    }
}