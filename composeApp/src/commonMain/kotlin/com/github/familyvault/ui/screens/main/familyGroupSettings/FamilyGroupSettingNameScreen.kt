package com.github.familyvault.ui.screens.main.familyGroupSettings

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import com.github.familyvault.repositories.IStoredChatMessageRepository
import com.github.familyvault.services.IFamilyGroupService
import com.github.familyvault.services.IFamilyGroupSessionService
import com.github.familyvault.services.ISavedFamilyGroupsService
import com.github.familyvault.ui.components.ContentWithAction
import com.github.familyvault.ui.components.overrides.Button
import com.github.familyvault.ui.components.overrides.TextField
import com.github.familyvault.ui.components.overrides.TopAppBar
import com.github.familyvault.ui.components.settings.SettingHeader
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.change_name_content
import familyvault.composeapp.generated.resources.setting_change_name_description_long
import familyvault.composeapp.generated.resources.setting_change_name_title
import familyvault.composeapp.generated.resources.text_field_group_name_label
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

class FamilyGroupSettingNameScreen : Screen {
    @Composable
    override fun Content() {
        val familyGroupService = koinInject<IFamilyGroupService>()
        val familyGroupSessionService = koinInject<IFamilyGroupSessionService>()
        val savedFamilyGroupsService = koinInject<ISavedFamilyGroupsService>()
        val coroutineScope = rememberCoroutineScope()

        var familyGroupName by remember { mutableStateOf(familyGroupSessionService.getFamilyGroupName()) }
        var currentFamilyGroupName by remember { mutableStateOf(familyGroupSessionService.getFamilyGroupName()) }
        var isChangingFamilyGroupName by remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                TopAppBar(
                    stringResource(Res.string.setting_change_name_title),
                    icon = Icons.Outlined.Edit,
                )
            }) { paddings ->
            ContentWithAction(
                modifier = Modifier.padding(AdditionalTheme.spacings.screenPadding)
                    .padding(paddings),
                content = {
                    SettingHeader(
                        description = stringResource(Res.string.setting_change_name_description_long),
                    )
                    TextField(
                        enabled = !isChangingFamilyGroupName,
                        value = familyGroupName,
                        onValueChange = { familyGroupName = it },
                        label = stringResource(Res.string.text_field_group_name_label)
                    )
                }, actionButton = {
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
                                savedFamilyGroupsService.changeFamilyGroupName(
                                    familyGroupSessionService.getContextId(),
                                    familyGroupName
                                )
                                currentFamilyGroupName = familyGroupName
                                isChangingFamilyGroupName = false
                            }
                        })
                })
        }
    }
}
