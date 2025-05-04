package com.github.familyvault.ui.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.github.familyvault.models.enums.chat.ChatThreadType
import com.github.familyvault.states.ITaskListState
import com.github.familyvault.ui.components.overrides.NavigationBar
import com.github.familyvault.ui.screens.main.chat.ChatThreadEditScreen
import com.github.familyvault.ui.screens.main.tasks.task.TaskNewScreen
import com.github.familyvault.ui.theme.AppTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.chat_create_new
import familyvault.composeapp.generated.resources.task_new
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

class MainScreen : Screen {
    @Composable
    override fun Content() {
        TabNavigator(ChatTab) {
            // Workaround błędu w Jetpack Compose powodujący to, że ekran nie dostostoswuje się
            // dynamicznie do motywu systemu. Zostanie naprawiony w jetpack compose 1.8.0-alpha06.
            // TODO: usunąć po naprawieniu blędu w compose.
            AppTheme {
                Scaffold(
                    bottomBar = {
                        NavigationBar(
                            ChatTab, FilesCabinetTab, TaskTab
                        )
                    },
                    floatingActionButton = {
                        FloatingCurrentTabActionButton()
                    }
                ) { paddingValues ->
                    Box(
                        modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding())
                    ) {
                        CurrentTab()
                    }
                }
            }
        }
    }

    @Composable
    private fun FloatingCurrentTabActionButton() {
        val tabNavigator = LocalTabNavigator.current

        when (tabNavigator.current) {
            is ChatTab -> FloatingChatActionButton()
            is TaskTab -> FloatingTaskActionButton()
        }
    }

    @Composable
    private fun FloatingChatActionButton() {
        val navigator = LocalNavigator.currentOrThrow
        FloatingActionButton(onClick = {
            navigator.parent?.push(
                ChatThreadEditScreen(
                    ChatThreadType.GROUP,
                    chatThread = null,
                )
            )
        }) {
            Icon(
                Icons.Filled.GroupAdd,
                stringResource(Res.string.chat_create_new),
            )
        }
    }

    @Composable
    private fun FloatingTaskActionButton() {
        val taskListState = koinInject<ITaskListState>()
        val navigator = LocalNavigator.currentOrThrow

        taskListState.selectedTaskList?.let {
            FloatingActionButton(onClick = {
                navigator.parent?.push(TaskNewScreen(requireNotNull(taskListState.selectedTaskList).id))
            }) {
                Icon(
                    Icons.Filled.AddTask,
                    stringResource(Res.string.task_new),
                )
            }
        }
    }
}
