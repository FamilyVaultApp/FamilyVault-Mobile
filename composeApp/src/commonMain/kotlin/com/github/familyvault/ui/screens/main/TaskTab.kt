package com.github.familyvault.ui.screens.main

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Task
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.github.familyvault.ui.components.SettingsIconButton
import com.github.familyvault.ui.components.overrides.TopAppBar
import com.github.familyvault.ui.screens.main.familyGroupSettings.FamilyGroupSettingsScreen
import com.github.familyvault.ui.screens.main.tasks.TasksContent
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.task_board_tab
import org.jetbrains.compose.resources.stringResource

object TaskTab : Tab {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Column {
            TopAppBar(
                stringResource(Res.string.task_board_tab),
                actions = {
                    SettingsIconButton {
                        navigator.parent?.push(FamilyGroupSettingsScreen())
                    }
                },
            )
            TasksContent()
        }
    }

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(Res.string.task_board_tab)
            val icon = rememberVectorPainter(Icons.Filled.Task)

            return remember {
                TabOptions(
                    index = 2u,
                    title = title,
                    icon = icon
                )
            }
        }
}