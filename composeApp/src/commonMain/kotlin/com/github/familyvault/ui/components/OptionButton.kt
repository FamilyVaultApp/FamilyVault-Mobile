package com.github.familyvault.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.github.familyvault.models.OptionType
import com.github.familyvault.models.backgroundColor
import com.github.familyvault.models.borderColor
import com.github.familyvault.models.iconTint
import com.github.familyvault.ui.components.typography.Headline3
import com.github.familyvault.ui.components.typography.Paragraph
import com.github.familyvault.ui.theme.AdditionalTheme

@Composable
fun OptionButton(
    title: String,
    content: String,
    icon: ImageVector,
    type: OptionType = OptionType.First,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    val borderColor = type.borderColor(isSelected)
    val backgroundColor = type.backgroundColor(isSelected)
    val iconTint = type.iconTint(isSelected)

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(AdditionalTheme.roundness.normalPercent))
            .border(
                BorderStroke(2.dp, borderColor),
                RoundedCornerShape(AdditionalTheme.roundness.normalPercent)
            )
            .clickable { onClick() }
            .padding(AdditionalTheme.spacings.normalPadding)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(AdditionalTheme.spacings.normalPadding)
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10))
                .size(40.dp)
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = title,
                modifier = Modifier.size(25.dp),
                tint = iconTint
            )
        }
        Column {
            Headline3(title)
            Paragraph(
                content,
                color = AdditionalTheme.colors.onPrimaryContainerSecondColor
            )
        }
    }
}
