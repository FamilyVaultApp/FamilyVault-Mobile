package com.github.familyvault.screens.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Task
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.github.familyvault.components.overrides.TopAppBar
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.task_board_tab
import org.jetbrains.compose.resources.stringResource

object TaskTab : Tab {
    @Composable
    override fun Content() {
        TopAppBar(
            stringResource(Res.string.task_board_tab)
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