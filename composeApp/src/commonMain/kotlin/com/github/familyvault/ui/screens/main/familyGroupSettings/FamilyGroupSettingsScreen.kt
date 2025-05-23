package com.github.familyvault.ui.screens.main.familyGroupSettings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.GroupAdd
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.models.enums.FamilyGroupMemberPermissionGroup
import com.github.familyvault.services.IFamilyGroupService
import com.github.familyvault.ui.components.SettingsCategory
import com.github.familyvault.ui.components.overrides.TopAppBar
import com.github.familyvault.ui.components.settings.SettingItem
import com.github.familyvault.ui.screens.main.ChangeFamilyGroupScreen
import com.github.familyvault.ui.screens.main.familyGroupSettings.familyGroupMember.AddMemberToFamilyGroupScreen
import com.github.familyvault.ui.screens.start.StartScreen
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.setting_no_permission
import familyvault.composeapp.generated.resources.confirm_button_content
import familyvault.composeapp.generated.resources.setting_add_member_short
import familyvault.composeapp.generated.resources.setting_add_member_title
import familyvault.composeapp.generated.resources.setting_change_family_group_short
import familyvault.composeapp.generated.resources.setting_change_family_group_title
import familyvault.composeapp.generated.resources.setting_change_name_description_short
import familyvault.composeapp.generated.resources.setting_change_name_title
import familyvault.composeapp.generated.resources.setting_join_or_create_family_group_short
import familyvault.composeapp.generated.resources.setting_join_or_create_family_group_title
import familyvault.composeapp.generated.resources.setting_members_short
import familyvault.composeapp.generated.resources.setting_members_title
import familyvault.composeapp.generated.resources.settings_category_management
import familyvault.composeapp.generated.resources.settings_category_other_family_groups
import familyvault.composeapp.generated.resources.settings_title
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

class FamilyGroupSettingsScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val familyGroupService = koinInject<IFamilyGroupService>()
        var isLoadingPermissionGroup by remember { mutableStateOf(true) }
        var showPermissionErrorDialog by remember { mutableStateOf(false) }
        var currentUserPermissionGroup by remember { mutableStateOf(FamilyGroupMemberPermissionGroup.Guest) }
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            isLoadingPermissionGroup = true
            currentUserPermissionGroup =
                familyGroupService.retrieveMyFamilyMemberData().permissionGroup
            isLoadingPermissionGroup = false
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    stringResource(Res.string.settings_title)
                )
            },
        ) { paddingValues ->
            if (!isLoadingPermissionGroup) {
                Column(
                    modifier = Modifier.padding(paddingValues)
                        .padding(vertical = AdditionalTheme.spacings.screenPadding)
                ) {
                    SettingsCategory(stringResource(Res.string.settings_category_other_family_groups)) {
                        SettingItem(
                            Icons.Outlined.GroupAdd,
                            title = stringResource(Res.string.setting_join_or_create_family_group_title),
                            description = stringResource(Res.string.setting_join_or_create_family_group_short),
                            onClick = {
                                navigator.push(StartScreen())
                            })
                        SettingItem(
                            Icons.Outlined.SwapHoriz,
                            title = stringResource(Res.string.setting_change_family_group_title),
                            description = stringResource(Res.string.setting_change_family_group_short),
                            onClick = {
                                navigator.push(ChangeFamilyGroupScreen())
                            })
                    }
                    SettingsCategory(stringResource(Res.string.settings_category_management)) {
                        if (currentUserPermissionGroup == FamilyGroupMemberPermissionGroup.Guardian) {
                            SettingItem(
                                Icons.Outlined.Edit,
                                title = stringResource(Res.string.setting_change_name_title),
                                description = stringResource(Res.string.setting_change_name_description_short),
                                onClick = {
                                    coroutineScope.launch {
                                        currentUserPermissionGroup = familyGroupService.retrieveMyFamilyMemberData().permissionGroup
                                        if (currentUserPermissionGroup == FamilyGroupMemberPermissionGroup.Guardian) {
                                            navigator.push(FamilyGroupSettingChangeNameScreen())
                                        } else {
                                            showPermissionErrorDialog = true
                                        }
                                    }
                                })
                        }
                        if (currentUserPermissionGroup == FamilyGroupMemberPermissionGroup.Guardian) {
                            SettingItem(
                                Icons.Outlined.PersonAdd,
                                title = stringResource(Res.string.setting_add_member_title),
                                description = stringResource(Res.string.setting_add_member_short),
                                onClick = {
                                    coroutineScope.launch {
                                        currentUserPermissionGroup = familyGroupService.retrieveMyFamilyMemberData().permissionGroup
                                        if (currentUserPermissionGroup == FamilyGroupMemberPermissionGroup.Guardian) {
                                            navigator.push(AddMemberToFamilyGroupScreen())
                                        } else {
                                            showPermissionErrorDialog = true
                                        }
                                    }
                                })
                        }
                        SettingItem(
                            Icons.Outlined.Groups,
                            title = stringResource(Res.string.setting_members_title),
                            description = stringResource(Res.string.setting_members_short),
                            onClick = {
                                navigator.push(FamilyGroupSettingMembersScreen())
                            })
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            if (showPermissionErrorDialog) {
                AlertDialog(
                    onDismissRequest = { showPermissionErrorDialog = false },
                    text = { Text(stringResource(Res.string.setting_no_permission)) },
                    confirmButton = {
                        TextButton(onClick = { showPermissionErrorDialog = false }) {
                            Text(stringResource(Res.string.confirm_button_content))
                        }
                    },
                )
            }
        }
    }

}
