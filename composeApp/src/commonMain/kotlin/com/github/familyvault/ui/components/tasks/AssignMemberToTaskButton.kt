package com.github.familyvault.ui.components.tasks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.github.familyvault.models.tasks.Task
import com.github.familyvault.states.IFamilyMembersState
import com.github.familyvault.ui.components.UserAvatar
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.task_assign
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun AssignMemberToTaskButton(task: Task, onClick: () -> Unit) {
    val familyMembersState = koinInject<IFamilyMembersState>()
    val assignedFamilyMember = task.content.assignedMemberPubKey?.let {
        familyMembersState.getFamilyMemberByPubKey(it)
    }

    Box(
        contentAlignment = Alignment.Center
    ) {
        assignedFamilyMember?.let {
            UserAvatar(
                it.fullname,
                modifier = Modifier.clickable(onClick = onClick)
            )
            return@Box
        }

        if (!task.content.completed) {
            IconButton(
                modifier = Modifier.size(AdditionalTheme.sizing.userAvatarSize),
                onClick = onClick
            ) {
                Icon(
                    Icons.AutoMirrored.Outlined.ArrowForward, stringResource(Res.string.task_assign)
                )
            }
        }
    }
}