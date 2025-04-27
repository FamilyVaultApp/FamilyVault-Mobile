package com.github.familyvault.ui.components.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.github.familyvault.ui.components.typography.Paragraph
import com.github.familyvault.ui.theme.AdditionalTheme

@Composable
fun TasksCategoryButton(
    title: String,
    icon: ImageVector? = null,
    selected: Boolean = false,
    onClick: () -> Unit
) {
    val backgroundColor = if (selected) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.primaryContainer
    }
    val contentColor = if (selected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Box(
        modifier = Modifier.height(AdditionalTheme.sizing.tasksCategoryButtonHeight)
            .clip(RoundedCornerShape(Int.MAX_VALUE.dp)).clickable(onClick = onClick)
            .background(backgroundColor).padding(
                vertical = AdditionalTheme.spacings.medium,
                horizontal = AdditionalTheme.spacings.medium * 2
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(icon, title, tint = contentColor)
            }
            Paragraph(title, color = contentColor)
        }
    }
}