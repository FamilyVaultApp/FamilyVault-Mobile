package com.github.familyvault.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.github.familyvault.models.FamilyMember
import com.github.familyvault.ui.components.typography.Paragraph
import com.github.familyvault.ui.theme.AdditionalTheme
import com.github.familyvault.utils.TextShortener

@Composable
fun FamilyMemberEntry(
    familyMember: FamilyMember,
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
            CharacterInCircle(firstName = familyMember.firstname)
            Paragraph(TextShortener.shortenText(familyMember.fullname, 30))
        }
        actionComponent()
    }
}