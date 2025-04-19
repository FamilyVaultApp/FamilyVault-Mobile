package com.github.familyvault.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontStyle
import com.github.familyvault.models.FamilyGroup
import com.github.familyvault.ui.components.typography.Paragraph
import com.github.familyvault.ui.components.typography.ParagraphMuted
import com.github.familyvault.ui.theme.AdditionalTheme

@Composable
fun FamilyGroupEntry(familyGroup: FamilyGroup) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.medium)
    ) {
        CharacterInCircle(familyGroup.name)
        Column {
            Paragraph(familyGroup.name)
            if (familyGroup.isDefault) {
                ParagraphMuted("Domyślny", fontStyle = FontStyle.Italic)
            }
        }

    }
}