package com.github.familyvault.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.github.familyvault.models.FamilyMember
import com.github.familyvault.ui.components.typography.Paragraph
import com.github.familyvault.ui.components.typography.ParagraphMuted
import com.github.familyvault.ui.theme.AdditionalTheme
import com.github.familyvault.utils.TextShortener
import familyvault.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.stringResource
import familyvault.composeapp.generated.resources.chat_create_group_user_is_member
import familyvault.composeapp.generated.resources.chat_create_group_user_is_not_member

@Composable
fun FamilyMemberEntry(
    familyMember: FamilyMember,
    additionalDescription: @Composable () -> Unit = {},
    actionComponent: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.defaultMinSize(minHeight = AdditionalTheme.sizing.entryMinSize)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.medium),
        ) {
            UserAvatar(firstName = familyMember.firstname)
            Column()
            {
                Paragraph(TextShortener.shortenText(familyMember.fullname, 30))
                additionalDescription()
            }
        }
        actionComponent()
    }
}