package com.github.familyvault.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.familyvault.models.FamilyMember
import com.github.familyvault.ui.components.typography.Paragraph
import com.github.familyvault.ui.screens.main.modifyFamilyMember.ModifyFamilyMemberScreen
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.user_modification_description
import org.jetbrains.compose.resources.stringResource

@Composable
fun FamilyMemberEntry(
    familyMember: FamilyMember
) {
    val navigator = LocalNavigator.currentOrThrow

    Row(
        modifier = Modifier.height(AdditionalTheme.spacings.large).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.medium),
        ) {
            UserAvatar(firstName = familyMember.firstname)
            Paragraph(familyMember.fullname)
        }


        IconButton(onClick = {
            navigator.push(ModifyFamilyMemberScreen(familyMember))
        }) {
            Icon(
                Icons.Outlined.MoreHoriz,
                contentDescription = stringResource(Res.string.user_modification_description),
            )
        }
    }
}