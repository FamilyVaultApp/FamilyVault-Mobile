package com.github.familyvault.ui.components.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.github.familyvault.ui.components.typography.Paragraph
import com.github.familyvault.ui.components.typography.ParagraphMuted
import com.github.familyvault.ui.theme.AdditionalTheme

@Composable
fun SettingItem(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(
                vertical = AdditionalTheme.spacings.medium,
                horizontal = AdditionalTheme.spacings.screenPadding + AdditionalTheme.spacings.small
            )
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = description,
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.width(AdditionalTheme.spacings.medium * 2))

        Column {
            Paragraph(title)
            ParagraphMuted(description)
        }
    }
}

