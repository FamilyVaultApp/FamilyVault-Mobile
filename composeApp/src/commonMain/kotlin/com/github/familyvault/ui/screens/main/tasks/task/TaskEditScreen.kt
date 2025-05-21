package com.github.familyvault.ui.screens.main.tasks.task

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
import com.github.familyvault.forms.TaskForm
import com.github.familyvault.models.tasks.Task
import com.github.familyvault.services.ITaskService
import com.github.familyvault.states.ITaskListState
import com.github.familyvault.ui.components.ContentWithActionButton
import com.github.familyvault.ui.components.formsContent.TaskFormContent
import com.github.familyvault.ui.components.overrides.Button
import com.github.familyvault.ui.components.overrides.TopAppBar
import com.github.familyvault.ui.components.settings.DescriptionSection
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.task_edit_description
import familyvault.composeapp.generated.resources.user_modification_save_button
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

class TaskEditScreen(private val task: Task) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val taskService = koinInject<ITaskService>()
        val taskListState = koinInject<ITaskListState>()

        val form: TaskForm by remember { mutableStateOf(TaskForm()) }
        val coroutineScope = rememberCoroutineScope()
        var isEditing by remember { mutableStateOf(false) }

        LaunchedEffect(task) {
            form.setTitle(task.content.title)
            form.setDescription(task.content.description)
            form.setPubKeyOfAssignedMember(task.content.assignedMemberPubKey)
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    task.content.title,
                    icon = Icons.Outlined.Task,
                )
            }
        )
        { paddingValues ->
            ContentWithActionButton(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(AdditionalTheme.spacings.screenPadding),
                content = {
                    DescriptionSection(stringResource(Res.string.task_edit_description))
                    TaskFormContent(form)
                },
                actionButton = {
                    Button(
                        stringResource(Res.string.user_modification_save_button),
                        modifier = Modifier.fillMaxWidth(),
                        enabled = form.isFormValid() && !isEditing,
                        onClick = {
                            isEditing = true
                            coroutineScope.launch {
                                taskService.updateTask(
                                    task.id,
                                    form.title,
                                    form.description,
                                    form.pubKeyOfAssignedMember
                                )
                                taskListState.populateTaskFormTaskListFromServices()
                                isEditing = false
                                navigator.pop()
                            }
                        }
                    )
                }
            )
        }
    }
}
