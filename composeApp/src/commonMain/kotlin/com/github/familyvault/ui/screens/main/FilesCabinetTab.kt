package com.github.familyvault.ui.screens.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
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
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.file_cabinet_tab
import org.jetbrains.compose.resources.stringResource

object FilesCabinetTab : Tab {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        TopAppBar(
            stringResource(Res.string.file_cabinet_tab),
            actions = {
                SettingsIconButton {
                    navigator.parent?.push(FamilyGroupSettingsScreen())
                }
            },
        )
    }

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(Res.string.file_cabinet_tab)
            val icon = rememberVectorPainter(Icons.Filled.Folder)

            return remember {
                TabOptions(
                    index = 1u,
                    title = title,
                    icon = icon
                )
            }
        }
}