package com.github.familyvault.screens.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Task
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.github.familyvault.components.typography.Headline3

object TaskTab : Tab {
    @Composable
    override fun Content() {
        Headline3("Task list screen")
    }

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Filled.Task)

            return remember {
                TabOptions (
                    index = 2u,
                    title = "Task list",
                    icon = icon
                )
            }
        }
}