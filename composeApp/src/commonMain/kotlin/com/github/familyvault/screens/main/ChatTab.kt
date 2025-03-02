package com.github.familyvault.screens.main

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.github.familyvault.components.overrides.TopAppBar
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.chats_tab
import org.jetbrains.compose.resources.stringResource

object ChatTab : Tab {
    @Composable
    override fun Content() {
        Column {
            TopAppBar(
                stringResource(Res.string.chats_tab)
            )
        }
    }

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(Res.string.chats_tab)
            val icon = rememberVectorPainter(Icons.Filled.Folder)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }
}