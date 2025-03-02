package com.github.familyvault.screens.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.github.familyvault.components.typography.Headline3

object ChatTab : Tab {
    @Composable
    override fun Content() {
        Headline3("Chat main screen")
    }

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Filled.Folder)

            return remember {
                TabOptions(
                    index = 0u,
                    title = "Chat",
                    icon = icon
                )
            }
        }
}