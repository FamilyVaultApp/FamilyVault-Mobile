package com.github.familyvault.ui.components.tasks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.github.familyvault.models.tasks.Task
import com.github.familyvault.services.ITaskService
import com.github.familyvault.states.IFamilyMembersState
import com.github.familyvault.ui.components.FamilyMemberPicker
import com.github.familyvault.ui.components.overrides.Button
import com.github.familyvault.ui.components.typography.Headline3
import com.github.familyvault.ui.components.typography.ParagraphMuted
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.assigned_person
import familyvault.composeapp.generated.resources.edit_button_content
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignMemberToTaskBottomSheet(task: Task, onDismissRequest: () -> Unit) {
    val familyMembersState = koinInject<IFamilyMembersState>()
    val taskService = koinInject<ITaskService>()

    val sheetState = rememberModalBottomSheetState()
    var selectedPubKey by remember { mutableStateOf(task.content.assignedMemberPubKey) }
    var isAssigning by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheet(onDismissRequest = onDismissRequest, sheetState = sheetState) {
        Column(
            modifier = Modifier.padding(horizontal = AdditionalTheme.spacings.screenPadding),
            verticalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.medium)
        ) {
            Headline3(task.content.title)
            ParagraphMuted(task.content.description)
            FamilyMemberPicker(
                label = stringResource(Res.string.assigned_person),
                selectedPubKey = selectedPubKey,
                familyMembers = familyMembersState.getAllFamilyMembers(),
                onPick = {
                    selectedPubKey = it?.publicKey
                }
            )
            Spacer(modifier = Modifier.height(AdditionalTheme.spacings.large))
            Button(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.edit_button_content),
                enabled = !isAssigning,
                onClick = {
                    coroutineScope.launch {
                        isAssigning = true
                        taskService.updateTask(
                            task.id,
                            task.content.copy(assignedMemberPubKey = selectedPubKey)
                        )
                        isAssigning = false
                        onDismissRequest()
                    }
                }
            )
        }
    }
}