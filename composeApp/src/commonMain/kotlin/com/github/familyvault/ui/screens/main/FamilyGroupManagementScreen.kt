package com.github.familyvault.ui.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.models.FamilyMember
import com.github.familyvault.services.IFamilyGroupService
import com.github.familyvault.services.IFamilyGroupSessionService
import com.github.familyvault.ui.components.FamilyMemberEntry
import com.github.familyvault.ui.components.overrides.Button
import com.github.familyvault.ui.components.overrides.TextField
import com.github.familyvault.ui.components.overrides.TopAppBar
import com.github.familyvault.ui.components.typography.Headline3
import com.github.familyvault.ui.screens.main.addFamilyMember.AddMemberToFamilyGroupScreen
import com.github.familyvault.ui.screens.main.modifyFamilyMember.ModifyFamilyMemberScreen
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.change_name_content
import familyvault.composeapp.generated.resources.family_group_add_new_member
import familyvault.composeapp.generated.resources.family_group_management_title
import familyvault.composeapp.generated.resources.family_group_members
import familyvault.composeapp.generated.resources.text_field_group_name_label
import familyvault.composeapp.generated.resources.user_modification_description
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

class FamilyGroupManagementScreen : Screen {
    @Composable
    override fun Content() {
        Scaffold(
            topBar = {
                TopAppBar(
                    stringResource(Res.string.family_group_management_title),
                    showManagementButton = false
                )
            },
        ) { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues)
                    .padding(horizontal = AdditionalTheme.spacings.screenPadding)
            ) {
                FamilyGroupNameEdit()
                FamilyGroupMembers()
                AddFamilyGroupMemberButton()
            }
        }
    }

    @Composable
    private fun FamilyGroupNameEdit() {
        val familyGroupService = koinInject<IFamilyGroupService>()
        val familyGroupSessionService = koinInject<IFamilyGroupSessionService>()
        val coroutineScope = rememberCoroutineScope()

        var familyGroupName by remember { mutableStateOf(familyGroupSessionService.getFamilyGroupName()) }
        var currentFamilyGroupName by remember { mutableStateOf(familyGroupSessionService.getFamilyGroupName()) }
        var isChangingFamilyGroupName by remember { mutableStateOf(false) }

        Column(
            verticalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.medium),
            modifier = Modifier.fillMaxWidth().padding(vertical = AdditionalTheme.spacings.large),
        ) {
            TextField(
                enabled = !isChangingFamilyGroupName,
                value = familyGroupName,
                onValueChange = { familyGroupName = it },
                label = stringResource(Res.string.text_field_group_name_label)
            )
            Button(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.change_name_content),
                enabled = familyGroupName != currentFamilyGroupName && familyGroupName.isNotBlank() && !isChangingFamilyGroupName,
                onClick = {
                    coroutineScope.launch {
                        isChangingFamilyGroupName = true
                        familyGroupService.renameCurrentFamilyGroup(
                            name = familyGroupName
                        )
                        familyGroupSessionService.updateFamilyGroupName(familyGroupName)
                        currentFamilyGroupName = familyGroupName
                        isChangingFamilyGroupName = false
                    }
                })
        }
    }

    @Composable
    private fun FamilyGroupMembers() {
        val navigator = LocalNavigator.currentOrThrow
        val familyGroupService = koinInject<IFamilyGroupService>()
        val familyGroupMembers = remember { mutableStateListOf<FamilyMember>() }

        var isLoadingMembers by remember { mutableStateOf(true) }

        LaunchedEffect(Unit) {
            isLoadingMembers = true
            familyGroupMembers.addAll(familyGroupService.retrieveFamilyGroupMembersList())
            isLoadingMembers = false
        }

        Column {
            Headline3(stringResource(Res.string.family_group_members))
            Column(
                modifier = Modifier.padding(vertical = 15.dp),
                verticalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.medium)
            ) {
                if (isLoadingMembers) {
                    Box(
                        modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    familyGroupMembers.forEach {
                        FamilyMemberEntry(it) {
                            IconButton(onClick = {
                                navigator.push(ModifyFamilyMemberScreen(it))
                            }) {
                                Icon(
                                    Icons.Outlined.MoreHoriz,
                                    contentDescription = stringResource(Res.string.user_modification_description),
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun AddFamilyGroupMemberButton() {
        val navigator = LocalNavigator.currentOrThrow

        Button(
            text = stringResource(Res.string.family_group_add_new_member),
            icon = Icons.Filled.Add,
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                navigator.push(AddMemberToFamilyGroupScreen())
            })
    }
}
