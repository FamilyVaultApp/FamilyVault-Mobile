package com.github.familyvault.screens.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.github.familyvault.components.overrides.TopAppBar
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.file_cabinet_tab
import org.jetbrains.compose.resources.stringResource

object FilesCabinetTab : Tab {
    @Composable
    override fun Content() {
        TopAppBar(
            stringResource(Res.string.file_cabinet_tab)
        )
    }

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(Res.string.file_cabinet_tab)
            val icon = rememberVectorPainter(Icons.AutoMirrored.Filled.Chat)

            return remember {
                TabOptions(
                    index = 1u,
                    title = title,
                    icon = icon
                )
            }
        }
}