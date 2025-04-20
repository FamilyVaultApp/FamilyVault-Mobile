package com.github.familyvault.ui.screens.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Task
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.github.familyvault.ui.components.overrides.TopAppBar
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.task_board_tab
import org.jetbrains.compose.resources.stringResource

object TaskTab : Tab {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        TopAppBar(
            stringResource(Res.string.task_board_tab),
            showManagementButton = true,
            onManagementButtonClick = {
                if (navigator.parent != null) {
                    navigator.parent?.push(FamilyGroupManagementScreen())
                } else {
                    navigator.push(FamilyGroupManagementScreen())
                }
            }
        )
    }

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(Res.string.task_board_tab)
            val icon = rememberVectorPainter(Icons.Filled.Task)

            return remember {
                TabOptions (
                    index = 2u,
                    title = title,
                    icon = icon
                )
            }
        }
}