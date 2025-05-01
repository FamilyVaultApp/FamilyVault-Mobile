package com.github.familyvault.ui.screens.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.models.FamilyGroup
import com.github.familyvault.services.IFamilyGroupSessionService
import com.github.familyvault.services.ISavedFamilyGroupsService
import com.github.familyvault.ui.components.ContentWithActionButton
import com.github.familyvault.ui.components.FamilyGroupEntry
import com.github.familyvault.ui.components.dialogs.CircularProgressIndicatorDialog
import com.github.familyvault.ui.components.overrides.Button
import com.github.familyvault.ui.components.overrides.TopAppBar
import com.github.familyvault.ui.components.settings.DescriptionSection
import com.github.familyvault.ui.screens.start.StartScreen
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.select_family_group_changing
import familyvault.composeapp.generated.resources.select_family_group_description
import familyvault.composeapp.generated.resources.select_family_group_join_or_create_button
import familyvault.composeapp.generated.resources.setting_change_family_group_title
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

class ChangeFamilyGroupScreen : Screen {
    @Composable
    override fun Content() {
        val savedFamilyGroupsService = koinInject<ISavedFamilyGroupsService>()
        val familyGroupSessionService = koinInject<IFamilyGroupSessionService>()
        val navigator = LocalNavigator.currentOrThrow

        var isLoading by mutableStateOf(false)
        var isChangingFamilyGroup by mutableStateOf(false)
        val familyGroups = mutableStateListOf<FamilyGroup>()
        val currentContextId =
            familyGroupSessionService.takeIf { it.isSessionAssigned() }?.getContextId()
        val coroutineScope = rememberCoroutineScope()


        LaunchedEffect(Unit) {
            isLoading = true
            familyGroups.addAll(savedFamilyGroupsService.getAllSavedFamilyGroups())
            isLoading = false
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = stringResource(Res.string.setting_change_family_group_title),
                    Icons.Outlined.Groups,
                )
            }) { paddings ->

            if (isChangingFamilyGroup) {
                CircularProgressIndicatorDialog(stringResource(Res.string.select_family_group_changing))
            }

            ContentWithActionButton(
                modifier = Modifier.padding(paddings)
                    .padding(vertical = AdditionalTheme.spacings.screenPadding),
                content = {
                    DescriptionSection(
                        description = stringResource(Res.string.select_family_group_description),
                        modifier = Modifier.padding(horizontal = AdditionalTheme.spacings.screenPadding)
                    )
                    Column {
                        familyGroups.map {
                            val isCurrentFamilyGroup = it.contextId == currentContextId

                            FamilyGroupEntry(
                                it, isCurrentFamilyGroup, onSelect = {
                                    if (isCurrentFamilyGroup) {
                                        return@FamilyGroupEntry
                                    }

                                    coroutineScope.launch {
                                        isChangingFamilyGroup = true
                                        familyGroupSessionService.disconnect()
                                        familyGroupSessionService.assignSession(
                                            familyGroupCredential = savedFamilyGroupsService.getSavedFamilyGroupCredentialByContextId(
                                                it.contextId
                                            )
                                        )
                                        familyGroupSessionService.connect()
                                        isChangingFamilyGroup = false
                                        navigator.replaceAll(MainScreen())
                                    }
                                },
                                onSetDefault = {
                                    if (it.isDefault) {
                                        return@FamilyGroupEntry
                                    }

                                    coroutineScope.launch {
                                        isLoading = true
                                        savedFamilyGroupsService.changeDefaultFamilyGroupCredential(
                                            it.contextId
                                        )
                                        familyGroups.clear()
                                        familyGroups.addAll(savedFamilyGroupsService.getAllSavedFamilyGroups())
                                        isLoading = false
                                    }
                                }
                            )
                        }
                    }
                },
                actionButton = {
                    CreateOrJoinToFamilyGroupButton()
                }
            )
        }
    }

    @Composable
    private fun CreateOrJoinToFamilyGroupButton() {
        val navigator = LocalNavigator.currentOrThrow

        Button(
            text = stringResource(Res.string.select_family_group_join_or_create_button),
            modifier = Modifier.padding(horizontal = AdditionalTheme.spacings.screenPadding)
                .fillMaxWidth(),
            onClick = {
                navigator.push(StartScreen())
            })
    }
}