package com.github.familyvault.screens.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.github.familyvault.components.typography.Headline3

object FilesCabinetTab : Tab {
    @Composable
    override fun Content() {
        Headline3("Files Cabinet screen")
    }

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.AutoMirrored.Filled.Chat)

            return remember {
                TabOptions(
                    index = 1u,
                    title = "Files cabinet",
                    icon = icon
                )
            }
        }
}