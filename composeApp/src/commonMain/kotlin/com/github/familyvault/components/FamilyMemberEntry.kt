package com.github.familyvault.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.familyvault.components.typography.Paragraph
import com.github.familyvault.models.FamilyMember
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.family_group_delete_member
import org.jetbrains.compose.resources.stringResource

@Composable
fun FamilyMemberEntry(
    familyMember: FamilyMember
) {
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
        IconButton(onClick = {}) {
            Icon(Icons.Outlined.Delete, stringResource(Res.string.family_group_delete_member))
        }
    }
}