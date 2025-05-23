package com.github.familyvault.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import com.github.familyvault.models.FamilyGroup
import com.github.familyvault.ui.components.typography.Paragraph
import com.github.familyvault.ui.components.typography.ParagraphMuted
import com.github.familyvault.ui.theme.AdditionalTheme
import familyvault.composeapp.generated.resources.Res
import familyvault.composeapp.generated.resources.as_with_spaces
import familyvault.composeapp.generated.resources.family_group_default
import familyvault.composeapp.generated.resources.family_group_no_default
import familyvault.composeapp.generated.resources.is_actual
import familyvault.composeapp.generated.resources.is_default
import org.jetbrains.compose.resources.stringResource

@Composable
fun FamilyGroupEntry(
    familyGroup: FamilyGroup,
    isCurrentFamilyGroup: Boolean,
    onSelect: () -> Unit,
    onSetDefault: () -> Unit,
) {
    val familyGroupInfo = listOfNotNull(
        if (isCurrentFamilyGroup) stringResource(Res.string.is_actual) else null,
        if (familyGroup.isDefault) stringResource(Res.string.is_default) else null
    )
    val backgroundColor = if (isCurrentFamilyGroup) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        Color.Unspecified
    }

    Row(
        modifier = Modifier.defaultMinSize(minHeight = AdditionalTheme.sizing.entryMinSize)
            .clickable(onClick = onSelect).background(
                backgroundColor
            ).fillMaxWidth().padding(
                vertical = AdditionalTheme.spacings.medium,
                horizontal = AdditionalTheme.spacings.screenPadding
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f, fill = true),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.medium)
        ) {
            Icon(
                if (isCurrentFamilyGroup) {
                    Icons.Filled.Group
                } else {
                    Icons.Outlined.Group
                },
                contentDescription = familyGroup.name,
                tint = if (isCurrentFamilyGroup) {
                    MaterialTheme.colorScheme.primary
                } else {
                    LocalContentColor.current
                }
            )
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Paragraph(
                        text = familyGroup.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    ParagraphMuted(
                        text = stringResource(Res.string.as_with_spaces) + familyGroup.firstname,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (familyGroupInfo.isNotEmpty()) {
                    ParagraphMuted(
                        text = familyGroupInfo.joinToString(),
                        fontStyle = FontStyle.Italic,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
        IconButton(
            onClick = onSetDefault
        )
        {
            Icon(
                if (familyGroup.isDefault) {
                    Icons.Filled.Star
                } else {
                    Icons.Outlined.StarOutline
                },
                contentDescription = if (familyGroup.isDefault) {
                    stringResource(Res.string.family_group_default)
                } else {
                    stringResource(Res.string.family_group_no_default)
                },
                tint = if (familyGroup.isDefault) {
                    MaterialTheme.colorScheme.primary
                } else {
                    LocalContentColor.current
                }
            )
        }
    }
}