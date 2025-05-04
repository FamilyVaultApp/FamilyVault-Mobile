package com.github.familyvault.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.familyvault.models.FamilyMember
import com.github.familyvault.models.FamilyMemberPickerEntry
import com.github.familyvault.ui.components.overrides.TextField
import com.github.familyvault.ui.components.typography.Paragraph
import com.github.familyvault.ui.theme.AdditionalTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FamilyMemberPicker(
    label: String,
    familyMembers: List<FamilyMember>,
    onPick: (FamilyMember?) -> Unit,
    selectedPubKey: String?,
    supportingText: @Composable (() -> Unit)? = null,
) {
    val avatarSize = (AdditionalTheme.sizing.userAvatarSize.value * 0.875).dp
    val entries = remember { mutableStateListOf(FamilyMemberPickerEntry()) }
    var expanded by remember { mutableStateOf(false) }
    val selectedOption =
        entries.firstOrNull { it.familyMember?.publicKey == selectedPubKey } ?: entries.first()

    LaunchedEffect(familyMembers) {
        entries.removeAll { it.familyMember != null }
        familyMembers.forEach {
            entries.add(FamilyMemberPickerEntry(it))
        }
    }

    ExposedDropdownMenuBox(
        modifier = Modifier.fillMaxWidth(),
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        TextField(
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
            label = label,
            readOnly = true,
            supportingText = supportingText,
            value = selectedOption.getName(),
            leadingIcon = selectedOption.familyMember?.let {
                {
                    Box(
                        modifier = Modifier.padding(start = AdditionalTheme.spacings.small)
                    ) {
                        UserAvatar(selectedOption.getName(), size = avatarSize)
                    }
                }
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
        )


        DropdownMenu(
            modifier = Modifier.exposedDropdownSize(),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            entries.forEach { option ->
                DropdownMenuItem(
                    leadingIcon =
                        option.familyMember?.let {
                            { UserAvatar(option.getName(), size = avatarSize) }
                        },
                    text = { Paragraph(option.getName()) },
                    onClick = {
                        onPick(option.familyMember)
                        expanded = false
                    }
                )
            }
        }
    }
}