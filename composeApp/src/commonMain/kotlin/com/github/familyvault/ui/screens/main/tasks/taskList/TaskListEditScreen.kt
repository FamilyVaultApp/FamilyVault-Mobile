package com.github.familyvault.ui.screens.main.tasks.taskList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Task
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.forms.TaskListForm
import com.github.familyvault.models.tasks.TaskList
import com.github.familyvault.services.ITaskService
import com.github.familyvault.states.ITaskListState
import com.github.familyvault.ui.components.ContentWithActionButton
import com.github.familyvault.ui.components.DangerButton
import com.github.familyvault.ui.components.dialogs.CircularProgressIndicatorDialog
import com.github.familyvault.ui.components.formsContent.TaskListFormContent
import com.github.familyvault.ui.components.overrides.Button
import com.github.familyvault.ui.components.overrides.TopAppBar
import com.github.familyvault.ui.components.settings.DescriptionSection
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.delete_button_content
import familyvault.composeapp.generated.resources.edit_button_content
import familyvault.composeapp.generated.resources.saving
import familyvault.composeapp.generated.resources.task_edit_list_description
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

class TaskListEditScreen(private val taskList: TaskList) : Screen {
    @Composable
    override fun Content() {
        val localNavigator = LocalNavigator.currentOrThrow
        val taskService = koinInject<ITaskService>()
        val taskListState = koinInject<ITaskListState>()

        val coroutineScope = rememberCoroutineScope()
        val taskListForm = remember { TaskListForm() }
        var isEditing by remember { mutableStateOf(false) }

        LaunchedEffect(taskList) {
            taskListForm.setNameWithoutTouching(taskList.name)
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    taskList.name,
                    icon = Icons.Outlined.Task,
                )
            },
            content = { paddingValues ->
                if (isEditing) {
                    CircularProgressIndicatorDialog(stringResource(Res.string.saving))
                }
                ContentWithActionButton(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(AdditionalTheme.spacings.screenPadding),
                    content = {
                        DescriptionSection(
                            stringResource(Res.string.task_edit_list_description)
                        )
                        TaskListFormContent(taskListForm)
                    },
                    actionButton = {
                        Column {
                            Button(
                                stringResource(Res.string.edit_button_content),
                                enabled = taskListForm.isFormValid() && !isEditing,
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                coroutineScope.launch {
                                    isEditing = true
                                    taskService.updateTaskList(taskList.id, taskListForm.name)
                                    taskListState.populateTaskListFromServices()
                                    if (taskList.id == taskListState.selectedTaskList?.id) {
                                        taskListState.selectTaskList(taskList.id)
                                    }
                                    isEditing = false
                                    localNavigator.pop()
                                }
                            }
                            DangerButton(
                                stringResource(Res.string.delete_button_content),
                                enabled = !isEditing,
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                coroutineScope.launch {
                                    isEditing = true
                                    if (taskList.id == taskListState.selectedTaskList?.id) {
                                        taskListState.unselectTaskList()
                                    }
                                    taskService.deleteTaskList(taskList.id)
                                    isEditing = false
                                    localNavigator.pop()
                                }
                            }
                        }
                    }
                )
            }
        )
    }
}