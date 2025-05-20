package com.github.familyvault.ui.screens.main.tasks.task

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
import com.github.familyvault.forms.TaskForm
import com.github.familyvault.services.ITaskService
import com.github.familyvault.states.ITaskListState
import com.github.familyvault.ui.components.ContentWithActionButton
import com.github.familyvault.ui.components.formsContent.TaskFormContent
import com.github.familyvault.ui.components.overrides.Button
import com.github.familyvault.ui.components.overrides.TopAppBar
import com.github.familyvault.ui.components.settings.DescriptionSection
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.create_button_content
import familyvault.composeapp.generated.resources.task_new_description
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

class TaskNewScreen(private val taskListId: String) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val taskService = koinInject<ITaskService>()
        val taskListState = koinInject<ITaskListState>()

        val form: TaskForm by remember { mutableStateOf(TaskForm()) }
        val coroutineScope = rememberCoroutineScope()
        var isCreatingTask by remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                taskListState.selectedTaskList?.name?.let {
                    TopAppBar(
                        it,
                        icon = Icons.Outlined.Task,
                    )
                }
            }) { paddingValues ->
            ContentWithActionButton(
                modifier = Modifier.padding(paddingValues)
                    .padding(AdditionalTheme.spacings.screenPadding), content = {
                    Column {
                        DescriptionSection(stringResource(Res.string.task_new_description))
                        TaskFormContent(form)
                    }
                }, actionButton = {
                    Button(
                        stringResource(Res.string.create_button_content),
                        modifier = Modifier.fillMaxWidth(),
                        enabled = form.isFormValid() && !isCreatingTask,
                        onClick = {
                            isCreatingTask = true
                            coroutineScope.launch {
                                taskService.createNewTask(
                                    taskListId,
                                    form.title,
                                    form.description,
                                    form.pubKeyOfAssignedMember
                                )
                                taskListState.populateTaskFormTaskListFromServices()
                                isCreatingTask = false
                                navigator.pop()
                            }
                        }
                    )
                }
            )
        }
    }
}