package com.github.familyvault.ui.screens.main.familyGroupSettings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Groups
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.models.FamilyMember
import com.github.familyvault.models.enums.FamilyGroupMemberPermissionGroup
import com.github.familyvault.services.IFamilyGroupService
import com.github.familyvault.ui.components.ContentWithActionButton
import com.github.familyvault.ui.components.FamilyMemberEntry
import com.github.familyvault.ui.components.overrides.Button
import com.github.familyvault.ui.components.overrides.TopAppBar
import com.github.familyvault.ui.components.settings.DescriptionSection
import com.github.familyvault.ui.components.typography.Paragraph
import com.github.familyvault.ui.screens.main.familyGroupSettings.familyGroupMember.AddMemberToFamilyGroupScreen
import com.github.familyvault.ui.screens.main.familyGroupSettings.familyGroupMember.ModifyFamilyMemberScreen
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.family_group_add_new_member
import familyvault.composeapp.generated.resources.setting_members_long
import familyvault.composeapp.generated.resources.setting_members_title
import familyvault.composeapp.generated.resources.user_modification_description
import familyvault.composeapp.generated.resources.user_permission_group_guardian
import familyvault.composeapp.generated.resources.user_permission_group_guest
import familyvault.composeapp.generated.resources.user_permission_group_member
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

class FamilyGroupSettingMembersScreen : Screen {

    @Composable
    private fun getPermissionGroupString(permissionGroup: FamilyGroupMemberPermissionGroup): String {
        return when (permissionGroup) {
            FamilyGroupMemberPermissionGroup.Guardian -> stringResource(Res.string.user_permission_group_guardian)
            FamilyGroupMemberPermissionGroup.Member -> stringResource(Res.string.user_permission_group_member)
            FamilyGroupMemberPermissionGroup.Guest -> stringResource(Res.string.user_permission_group_guest)
        }
    }

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val familyGroupService = koinInject<IFamilyGroupService>()
        val familyGroupMembers = remember { mutableStateListOf<FamilyMember>() }
        var currentFamilyGroupMemberPermissionGroup by remember { mutableStateOf(FamilyGroupMemberPermissionGroup.Guest)}
        var isLoadingMembers by remember { mutableStateOf(true) }

        LaunchedEffect(Unit) {
            isLoadingMembers = true
            familyGroupMembers.addAll(familyGroupService.retrieveFamilyGroupMembersList())
            currentFamilyGroupMemberPermissionGroup = familyGroupService.retrieveMyFamilyMemberData().permissionGroup
            isLoadingMembers = false
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    stringResource(Res.string.setting_members_title),
                    icon = Icons.Outlined.Groups,
                )
            }) { padding ->
            ContentWithActionButton(
                modifier = Modifier.padding(padding)
                    .padding(AdditionalTheme.spacings.screenPadding),
                content = {
                    DescriptionSection(
                        description = stringResource(Res.string.setting_members_long),
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.medium)
                    ) {
                        if (isLoadingMembers) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        } else {
                            familyGroupMembers.forEach { member ->
                                FamilyMemberEntry(
                                    familyMember = member,
                                    additionalDescription = { Paragraph(getPermissionGroupString(member.permissionGroup)) }
                                ) {
                                    IconButton(onClick = {
                                        navigator.push(ModifyFamilyMemberScreen(member))
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
                },
                actionButton = {
                    AddFamilyMemberButton(currentFamilyGroupMemberPermissionGroup)
                }
            )
        }
    }

    @Composable
    private fun AddFamilyMemberButton(currentUserPermissionGroup: FamilyGroupMemberPermissionGroup) {
        val navigator = LocalNavigator.currentOrThrow

        Button(
            text = stringResource(Res.string.family_group_add_new_member),
            icon = Icons.Filled.Add,
            modifier = Modifier.fillMaxWidth(),
            enabled = currentUserPermissionGroup == FamilyGroupMemberPermissionGroup.Guardian,
            onClick = {
                navigator.push(AddMemberToFamilyGroupScreen())
            })
    }
}

