package com.github.familyvault.components


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.github.familyvault.Constants
import com.github.familyvault.components.typography.Headline3
import com.github.familyvault.components.typography.Paragraph

enum class OptionButtonType {
    First,
    Second
}

@Composable
fun OptionButton(
    title: String,
    content: String,
    icon: ImageVector,
    type: OptionButtonType = OptionButtonType.First
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(Constants.normalRoundPercent))
            .border(
                BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
                RoundedCornerShape(Constants.normalRoundPercent)
            )
            .padding(Constants.normalPaddingSize)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Constants.normalPaddingSize)
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10))
                .size(40.dp)
                .background(
                    if (type == OptionButtonType.First)
                        MaterialTheme.colorScheme.tertiaryContainer
                    else MaterialTheme.colorScheme.tertiaryContainer
                ),
            contentAlignment = Alignment.Center
        )
        {
            Icon(
                icon,
                title,
                modifier = Modifier.size(25.dp),
                tint = if (type == OptionButtonType.First)
                    MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.primary
            )
        }
        Column {
            Headline3(title)
            Paragraph(content)
        }
    }
}