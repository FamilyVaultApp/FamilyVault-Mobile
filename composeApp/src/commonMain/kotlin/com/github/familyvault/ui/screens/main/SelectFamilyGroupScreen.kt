package com.github.familyvault.ui.screens.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import com.github.familyvault.models.FamilyGroup
import com.github.familyvault.services.ISavedFamilyGroupsService
import com.github.familyvault.ui.components.FamilyGroupEntry
import com.github.familyvault.ui.components.overrides.TopAppBar
import com.github.familyvault.ui.components.settings.SettingHeader
import com.github.familyvault.ui.theme.AdditionalTheme
import org.koin.compose.koinInject

class SelectFamilyGroupScreen : Screen {
    @Composable
    override fun Content() {
        val savedFamilyGroupsService = koinInject<ISavedFamilyGroupsService>()

        var isLoading by mutableStateOf(false)
        val familyGroups = mutableStateListOf<FamilyGroup>()


        LaunchedEffect(savedFamilyGroupsService) {
            isLoading = true
            familyGroups.addAll(savedFamilyGroupsService.getAllSavedFamilyGroups())
            isLoading = false
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = "Wybór grupy rodzinnej",
                    Icons.Outlined.Groups,
                    showManagementButton = false
                )
            }
        ) { paddings ->
            Column(
                modifier = Modifier.padding(paddings)
                    .padding(AdditionalTheme.spacings.screenPadding)
            ) {
                SettingHeader(
                    description = "Wybierz grupę rodzinną do której chcesz dołączyć",
                )
                Column {
                    familyGroups.map {
                        FamilyGroupEntry(it)
                    }
                }
            }
        }
    }
}