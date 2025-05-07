package com.github.familyvault.ui.screens.main.tasks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.models.enums.FamilyGroupMemberPermissionGroup
import com.github.familyvault.services.IFamilyGroupService
import com.github.familyvault.ui.components.HeaderIcon
import com.github.familyvault.ui.components.overrides.Button
import com.github.familyvault.ui.components.typography.Headline3
import com.github.familyvault.ui.components.typography.ParagraphMuted
import com.github.familyvault.ui.screens.main.tasks.taskList.TaskNewListScreen
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.create_button_content
import familyvault.composeapp.generated.resources.no_task_list_description
import familyvault.composeapp.generated.resources.no_task_list_description_without_permission
import familyvault.composeapp.generated.resources.no_task_list_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun TasksNoListsContent() {
    val localNavigator = LocalNavigator.currentOrThrow
    val familyGroupService = koinInject<IFamilyGroupService>()
    var myPermissionGroup: FamilyGroupMemberPermissionGroup? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        val me = familyGroupService.retrieveMyFamilyMemberData()
        myPermissionGroup = me.permissionGroup
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderIcon(
            Icons.AutoMirrored.Filled.ListAlt, size = AdditionalTheme.sizing.headerIconNormal
        )
        Column(
            modifier = Modifier.padding(vertical = AdditionalTheme.spacings.medium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Headline3(
                stringResource(Res.string.no_task_list_title),
                fontWeight = FontWeight.SemiBold
            )
            myPermissionGroup?.let {
                ParagraphMuted(
                    stringResource(
                        if (it == FamilyGroupMemberPermissionGroup.Guardian) Res.string.no_task_list_description else Res.string.no_task_list_description_without_permission
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(250.dp)
                )
            }
        }
        myPermissionGroup?.let {
            if (it == FamilyGroupMemberPermissionGroup.Guardian) {
                Button(stringResource(Res.string.create_button_content)) {
                    localNavigator.parent?.push(
                        TaskNewListScreen()
                    )
                }
            }
        }
    }
}