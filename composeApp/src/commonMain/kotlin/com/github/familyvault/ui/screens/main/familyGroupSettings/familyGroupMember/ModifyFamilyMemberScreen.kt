package com.github.familyvault.ui.screens.main.familyGroupSettings.familyGroupMember

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
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
import com.github.familyvault.models.FamilyMember
import com.github.familyvault.models.enums.FamilyGroupMemberPermissionGroup
import com.github.familyvault.repositories.IFamilyGroupCredentialsRepository
import com.github.familyvault.services.IChatService
import com.github.familyvault.services.IFamilyGroupService
import com.github.familyvault.services.IFamilyGroupSessionService
import com.github.familyvault.services.IFamilyMemberPermissionGroupService
import com.github.familyvault.ui.components.dialogs.RemoveFamilyMemberDialog
import com.github.familyvault.ui.components.overrides.Button
import com.github.familyvault.ui.components.overrides.TopAppBar
import com.github.familyvault.ui.components.typography.Headline3
import com.github.familyvault.ui.components.typography.Paragraph
import com.github.familyvault.ui.screens.main.ChangeFamilyGroupScreen
import com.github.familyvault.ui.screens.main.MainScreen
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.user_modification_choose_permission_content
import familyvault.composeapp.generated.resources.user_modification_remove_user_button_content
import familyvault.composeapp.generated.resources.user_modification_save_button
import familyvault.composeapp.generated.resources.user_permission_group_guardian
import familyvault.composeapp.generated.resources.user_permission_group_guest
import familyvault.composeapp.generated.resources.user_permission_group_member
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

class ModifyFamilyMemberScreen(private val familyMember: FamilyMember) : Screen {
    data class PermissionOption(
        val label: String,
        val permissionGroup: FamilyGroupMemberPermissionGroup
    )

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val permissionGroupService = koinInject<IFamilyMemberPermissionGroupService>()
        val familyGroupSessionService = koinInject<IFamilyGroupSessionService>()
        val familyGroupService = koinInject<IFamilyGroupService>()
        val familyGroupCredentialsRepository = koinInject<IFamilyGroupCredentialsRepository>()
        val chatService = koinInject<IChatService>()
        val familyMembers = remember { mutableListOf<FamilyMember>() }

        val options = listOf(
            PermissionOption(
                stringResource(Res.string.user_permission_group_guardian),
                FamilyGroupMemberPermissionGroup.Guardian
            ),
            PermissionOption(
                stringResource(Res.string.user_permission_group_member),
                FamilyGroupMemberPermissionGroup.Member
            ),
            PermissionOption(
                stringResource(Res.string.user_permission_group_guest),
                FamilyGroupMemberPermissionGroup.Guest
            )
        )

        LaunchedEffect(Unit) {
            familyMembers.addAll(familyGroupService.retrieveFamilyGroupMembersList())
        }

        var savingChanges by remember { mutableStateOf(false) }
        var showDialog by remember { mutableStateOf(false) }
        var selectedPermissionGroup by remember { mutableStateOf(FamilyGroupMemberPermissionGroup.Guest) }
        val coroutineScope = rememberCoroutineScope()

        Scaffold(
            topBar = {
                TopAppBar(
                    familyMember.fullname,
                )
            },
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
                    .padding(AdditionalTheme.spacings.screenPadding),
                verticalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.medium)
            ) {
                Headline3(stringResource(Res.string.user_modification_choose_permission_content))
                options.forEachIndexed { index, option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { selectedPermissionGroup = option.permissionGroup }
                            .fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = selectedPermissionGroup == option.permissionGroup,
                            onClick = {
                                selectedPermissionGroup = option.permissionGroup
                            }
                        )
                        Spacer(modifier = Modifier.width(AdditionalTheme.spacings.large))
                        Paragraph(option.label)
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxHeight().padding(paddingValues)
                    .padding(AdditionalTheme.spacings.screenPadding),
                verticalArrangement = Arrangement.spacedBy(
                    AdditionalTheme.spacings.medium,
                    Alignment.Bottom
                ),
            ) {
                Button(
                    text = stringResource(Res.string.user_modification_save_button),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !savingChanges,
                    onClick = {
                        coroutineScope.launch {
                            savingChanges = true
                            permissionGroupService.changeFamilyMemberPermissionGroup(
                                familyMember.fullname,
                                selectedPermissionGroup
                            )
                            chatService.updateThreadsAfterUserPermissionChange(familyMember, familyMembers)
                            savingChanges = false
                            navigator.pop()
                        }
                    }
                )
                Button(
                    text = stringResource(Res.string.user_modification_remove_user_button_content),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !savingChanges,
                    containerColor = MaterialTheme.colorScheme.error,
                    onClick = {
                        showDialog = true
                    }
                )
            }
            if (showDialog) {
                RemoveFamilyMemberDialog(onConfirm = {
                    coroutineScope.launch {
                        familyGroupService.removeMemberFromCurrentFamilyGroup(
                            familyMember.publicKey
                        )
                        if (familyMember.publicKey == familyGroupSessionService.getPublicKey()) {
                            familyGroupCredentialsRepository.deleteCredential(
                                familyGroupSessionService.getContextId()
                            )
                            familyGroupSessionService.disconnect()
                            navigator.replaceAll(ChangeFamilyGroupScreen())
                        } else {
                            navigator.replace(MainScreen())
                        }
                    }
                    showDialog = false
                }, onDismiss = {
                    showDialog = false
                })
            }
        }
    }
}