package com.github.familyvault.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.models.FamilyMember
import com.github.familyvault.services.IFamilyGroupService
import com.github.familyvault.services.IFamilyGroupSessionService
import com.github.familyvault.ui.components.typography.Paragraph
import com.github.familyvault.ui.screens.main.MainScreen
import com.github.familyvault.ui.screens.start.StartScreen
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.cancel_button_content
import familyvault.composeapp.generated.resources.family_group_delete_member
import familyvault.composeapp.generated.resources.family_group_members
import familyvault.composeapp.generated.resources.text_field_group_name_label
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun FamilyMemberEntry(
    familyMember: FamilyMember
) {
    var showDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.height(45.dp).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            UserAvatar(firstName = familyMember.firstname)
            Paragraph(familyMember.fullname)
        }

        val familyGroupService: IFamilyGroupService = koinInject()
        val familyGroupSessionService: IFamilyGroupSessionService = koinInject()
        val contextId = familyGroupSessionService.getContextId()
        val coroutineScope = rememberCoroutineScope()
        val navigator = LocalNavigator.currentOrThrow

        IconButton(onClick = {
            showDialog = true
        }) {
            Icon(Icons.Outlined.Delete, contentDescription = stringResource(Res.string.family_group_delete_member))
        }

        if (showDialog) {
            AlertDialog(
                // TODO DOPASOWAĆ TREŚĆ
                onDismissRequest = { showDialog = false },
                title = { Text("Tytuł") },
                text = { Text("Treść") },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog = false
                        coroutineScope.launch {
                            familyGroupService.removeMemberFromFamilyGroup(contextId, familyMember.publicKey)
                            navigator.replaceAll(MainScreen())
                        }
                    }) {
                        Text("Potwierdź")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Anuluj")
                    }
                }
            )
        }
    }
}