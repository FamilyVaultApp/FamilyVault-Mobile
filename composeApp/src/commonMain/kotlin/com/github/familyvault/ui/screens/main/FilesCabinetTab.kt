package com.github.familyvault.ui.screens.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.github.familyvault.models.enums.FamilyGroupMemberPermissionGroup
import com.github.familyvault.services.IFamilyGroupService
import com.github.familyvault.ui.components.LoaderWithText
import com.github.familyvault.ui.components.SettingsIconButton
import com.github.familyvault.ui.components.overrides.TopAppBar
import com.github.familyvault.ui.screens.main.familyGroupSettings.FamilyGroupSettingsScreen
import com.github.familyvault.ui.screens.main.filesCabinet.FilesCabinetContent
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.file_cabinet_tab
import familyvault.composeapp.generated.resources.loading
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

object FilesCabinetTab : Tab {
    var selectedTabIndex by mutableStateOf(0)
    
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val familyGroupService = koinInject<IFamilyGroupService>()
        var currentUserPermissionGroup by remember { mutableStateOf(FamilyGroupMemberPermissionGroup.Guest) }
        var isLoading by remember { mutableStateOf(true) }
        LaunchedEffect(Unit) {
            isLoading = true
            currentUserPermissionGroup = familyGroupService.retrieveMyFamilyMemberData().permissionGroup
            isLoading = false
        }

        if (isLoading) {
            LoaderWithText(stringResource(Res.string.loading), modifier = Modifier.fillMaxSize())
        } else {
            Column {
                TopAppBar(
                    stringResource(Res.string.file_cabinet_tab),
                    actions = {
                        SettingsIconButton {
                            navigator.parent?.push(FamilyGroupSettingsScreen())
                        }
                    },
                )
                FilesCabinetContent(
                    selectedTabIndex = selectedTabIndex,
                    onTabIndexChanged = { selectedTabIndex = it },
                    currentUserPermissionGroup = currentUserPermissionGroup
                )
            }
        }
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